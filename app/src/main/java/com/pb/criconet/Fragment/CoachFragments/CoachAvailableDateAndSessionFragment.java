package com.pb.criconet.Fragment.CoachFragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.applandeo.materialcalendarview.EventDay;
import com.google.gson.Gson;
import com.pb.criconet.Activity.Coach.RegisterAsAnECoachActivity;
import com.pb.criconet.R;
import com.pb.criconet.adapter.EcoachingAdapter.SessionTimeListAdapter;
import com.pb.criconet.adapter.EcoachingAdapter.TimeAdaptercoachh;
import com.pb.criconet.databinding.FragmentCoachDateSesssionBinding;
import com.pb.criconet.databinding.FragmentCoachProfessionalInformationBinding;
import com.pb.criconet.model.Coaching.TimeSlot;
import com.pb.criconet.model.Coaching.updatedTimeSlot;
import com.pb.criconet.util.CustomLoaderView;
import com.pb.criconet.util.Global;
import com.pb.criconet.util.GridSpacingItemDecoration;
import com.pb.criconet.util.SessionManager;
import com.pb.criconet.util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;


public class CoachAvailableDateAndSessionFragment extends Fragment {

   FragmentCoachDateSesssionBinding fragmentCoachDateSesssionBinding;
    Long date;
    Animation slide_down,slide_up;
    private SharedPreferences prefs;
    private RequestQueue queue;
    CustomLoaderView loaderView;
    Date previousDate;
    private String year="";
    private String month="";
    private String day="";
    private String dateGot="";
    List<String> updatedDateList = new ArrayList<>();
    ArrayList<updatedTimeSlot> updatedSlotList;
    JSONArray updateJsonArray = new JSONArray();
    List<EventDay> events;
    private TimeSlot modelArrayList;
    String selectedTimeSlot = "", selectedTimeSlott = "",selectedTimeSlotUpdated="";
    private String dateGott = "";
    private String fromWhere="";

    public CoachAvailableDateAndSessionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fromWhere = getArguments().getString("FROM");

            //Toaster.customToast(fromWhere);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentCoachDateSesssionBinding = FragmentCoachDateSesssionBinding.inflate(inflater, container, false);

        // Retrieve the argument to set the button visibility
        boolean showSaveButton = getArguments() != null && getArguments().getBoolean("showSaveButton", false);
        fragmentCoachDateSesssionBinding.flSave.setVisibility(showSaveButton ? View.VISIBLE : View.GONE);

        return fragmentCoachDateSesssionBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        loaderView = CustomLoaderView.initialize(getActivity());
        queue = Volley.newRequestQueue(getActivity());

        // disable dates before today
        // disable dates before today
        Calendar min = Calendar.getInstance();
        previousDate = min.getTime();
        min.add(Calendar.DAY_OF_MONTH, -1);
        fragmentCoachDateSesssionBinding.calendarView.setMinimumDate(min);

        // Get current page date as a string
        String currentDate = fragmentCoachDateSesssionBinding.calendarView.getCurrentPageDate().getTime().toString();

        // Retrieve year, month, and day
        year = Global.getYear(currentDate);
        month = Global.getMonth(currentDate);
        day = Global.getDay(currentDate);

        // Check for null or empty values
        if (year != null && !year.isEmpty() && month != null && !month.isEmpty() && day != null && !day.isEmpty()) {
            try {
                int yearInt = Integer.parseInt(year);
                int monthInt = Integer.parseInt(month);
                int dayInt = Integer.parseInt(day);
                printDatesInMonth(yearInt, monthInt, dayInt);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                // Handle the error, e.g., show an error message or log the issue
            }
        } else {
            // Handle the case where any of the values are null or empty
            // e.g., log an error or use default values
            Log.e("DateError", "One of the date components is null or empty: year=" + year + ", month=" + month + ", day=" + day);
        }


        if(fromWhere.equalsIgnoreCase("1")){
            fragmentCoachDateSesssionBinding.flSave.setVisibility(View.VISIBLE);
        }else{
            fragmentCoachDateSesssionBinding.flSave.setVisibility(View.GONE);
        }



      // Toaster.customToast(fromWhere);
        if(fromWhere.equalsIgnoreCase("1")){

            if (Global.isOnline(getActivity())) {
                getUsersDetails();
            } else {
                Global.showDialog(getActivity());
            }
        }

        initView();
    }

    private void initView() {
        fragmentCoachDateSesssionBinding.rvSessionTime.setHasFixedSize(true);
        fragmentCoachDateSesssionBinding.rvSessionTime.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        fragmentCoachDateSesssionBinding.rvSessionTime.addItemDecoration(new GridSpacingItemDecoration(2, 20, false));

        fragmentCoachDateSesssionBinding.calendarView.setOnDayClickListener(eventDay -> {

            slide_down = AnimationUtils.loadAnimation(requireContext(),
                    R.anim.slide_down);
            fragmentCoachDateSesssionBinding.liSessionLayout.setAnimation(slide_down);
            fragmentCoachDateSesssionBinding.liSessionLayout.setVisibility(View.VISIBLE);

            dateGot = Global.getDateGotCoach(eventDay.getCalendar().getTime().toString());

            JSONObject jsonObject;
            updatedTimeSlot timeSlot;

//            if(updateJsonArray.length()==0){
//                if (Global.getDateGot(eventDay.getCalendar().getTime().toString()).equals(Global.getDateGot(previousDate.toString())) || eventDay.getCalendar().getTime().compareTo(previousDate) > 0) {
//                    if (Global.isOnline(getActivity())) {
//
//                        getDateSlote(dateGot, new ArrayList<>());
//                    } else {
//                        Global.showDialog(getActivity());
//                    }
//                } else {
//                    Toaster.customToast("Select enabled date");
//                }
//            }else{
                updatedSlotList = new ArrayList<>();
                updatedSlotList.clear();


                for (int i = 0; i < updateJsonArray.length(); i++) {
                    try {
                        jsonObject = updateJsonArray.getJSONObject(i);
                        if (jsonObject.has("available_date")) {
                            String avl_date = jsonObject.getString("available_date");

                            if (dateGot.equalsIgnoreCase(Global.getDateGotConvert(avl_date))) {
                                if (jsonObject.has("slot_id")) {
                                    JSONArray jsonArray1 = jsonObject.getJSONArray("slot_id");
                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                        JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                        timeSlot = new updatedTimeSlot(jsonObject1);
                                        updatedSlotList.add(timeSlot);
                                    }
                                }

                                break;

                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                if (Global.getDateGot(eventDay.getCalendar().getTime().toString()).equals(Global.getDateGot(previousDate.toString())) || eventDay.getCalendar().getTime().compareTo(previousDate) > 0) {
                    if (Global.isOnline(getActivity())) {
                        getDateSlote(dateGot, updatedSlotList);
                    } else {
                        Global.showDialog(getActivity());
                    }
                } else {
                    Toaster.customToast("Select enabled date");
                }
         //   }



        });

        fragmentCoachDateSesssionBinding.calendarView.setOnForwardPageChangeListener(() -> {
            dateGot="";
            if (fragmentCoachDateSesssionBinding.calendarView.getCurrentPageDate() != null) {
                Date currentPageDate = fragmentCoachDateSesssionBinding.calendarView.getCurrentPageDate().getTime();
                if (currentPageDate != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(currentPageDate);

                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero-based
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    printDatesInMonth(year, month, day);
                    //dateGot = Global.getDateGotCoach(currentPageDate.toString());
                } else {
                    Toaster.customToast("Current page date is null");
                }
            } else {
                Toaster.customToast("CalendarView's current page date is null");
            }

        });

        fragmentCoachDateSesssionBinding.calendarView.setOnPreviousPageChangeListener(() -> {

            dateGot="";
            if (fragmentCoachDateSesssionBinding.calendarView.getCurrentPageDate() != null) {
                Date currentPageDate = fragmentCoachDateSesssionBinding.calendarView.getCurrentPageDate().getTime();
                if (currentPageDate != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(currentPageDate);

                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero-based
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    printDatesInMonth(year, month, day);
                    //dateGot = Global.getDateGotCoach(currentPageDate.toString());
                } else {
                    Toaster.customToast("Current page date is null");
                }
            } else {
                Toaster.customToast("CalendarView's current page date is null");
            }
        });



        fragmentCoachDateSesssionBinding.flSave.setOnClickListener(view -> {
            selectedTimeSlot = selectedTimeSlott;

            if (Global.isOnline(getActivity())) {

                if(checkValidation()){
                    updateCoachAvailability();
                }

            } else {
                Global.showDialog(getActivity());
            }

        });


    }

    public void printDatesInMonth(int year, int month,int day) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(year, month, day);
        events = new ArrayList<>();
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //Toaster.customToast(daysInMonth+"");
        //cal.get
        for (int i = 0; i <= daysInMonth; i++) {
            System.out.println(fmt.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_MONTH, -1);

            //Toaster.customToast(updatedDateList.size()+"/");

            for (int j = 0; j < updatedDateList.size(); j++) {
                String dateGot = Global.getDateGot(cal.getTime().toString());
                String listDate = updatedDateList.get(j);

                if (dateGot != null && listDate != null && dateGot.equals(listDate)) {
                    events.add(new EventDay(Global.convertStringToCalendar(fmt.format(cal.getTime())), Global.getThreeDotss(getActivity())));
                    fragmentCoachDateSesssionBinding.calendarView.setEvents(events);
                }
            }


            events.add(new EventDay(Global.convertStringToCalendar(fmt.format(cal.getTime())), Global.getThreeDots(getActivity())));
            fragmentCoachDateSesssionBinding.calendarView.setEvents(events);
        }
    }


    private void getDateSlote(String dateGot, ArrayList<updatedTimeSlot> updatedSlotList) {
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_time_slot_data", response -> {
            Timber.d(response);
            Gson gson = new Gson();
            modelArrayList = gson.fromJson(response, TimeSlot.class);
            if (modelArrayList.getApiStatus().equalsIgnoreCase("200")) {

                //Toaster.customToast(updatedSlotList.size()+"//");

                fragmentCoachDateSesssionBinding.rvSessionTime.setAdapter(new TimeAdaptercoachh(getActivity(), modelArrayList.getData(), updatedSlotList, arrayListUser -> {
                    String[] namesArr = arrayListUser.toArray(new String[arrayListUser.size()]);
                    selectedTimeSlott = toCSV(namesArr);
                }));

            } else {
                dateGott = "";
                Toaster.customToast(modelArrayList.getErrors().getErrorText());
            }
        }, error -> {
            error.printStackTrace();
            Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("date", dateGot);
                System.out.println("data   " + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    public static String toCSV(String[] array) {
        String result = "";
        if (array.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (String s : array) {
                sb.append(s).append(",");
            }
            result = sb.deleteCharAt(sb.length() - 1).toString();
        } return result;
    }

    private boolean checkValidation(){
        //Toaster.customToast(selectedTimeSlot);
        if(dateGot.equalsIgnoreCase("")){
            Toaster.customToast(getResources().getString(R.string.Please_select_date_first));
            return false;
        }else if (selectedTimeSlot.isEmpty()){
            // && selectedTimeSlotUpdated.equalsIgnoreCase("")) {
            Toaster.customToast(getResources().getString(R.string.Please_select_time_first));
            return false;
        }
        return true;
    }

    private void updateCoachAvailability() {

        loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "update_coach_availability_date", response -> {
            loaderView.hideLoader();
            Timber.d(response);

            try {
                JSONObject jsonObject= new JSONObject(response);
                JSONObject data=null;
                if (jsonObject.getString("api_status").equalsIgnoreCase("200")) {
                    data=jsonObject.getJSONObject("data");
                    Toaster.customToast(data.getString("message"));
                    refreshData();
                    year = Global.getYear(fragmentCoachDateSesssionBinding.calendarView.getCurrentPageDate().getTime().toString());
                    month = Global.getMonth(fragmentCoachDateSesssionBinding.calendarView.getCurrentPageDate().getTime().toString());
                    day = Global.getDay(fragmentCoachDateSesssionBinding.calendarView.getCurrentPageDate().getTime().toString());
//                    if (Global.isOnline(mContext)) {
//                        getUsersDetails();
//                    } else {
//                        Global.showDialog(mActivity);
//                    }
                    printDatesInMonth(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
//                    if (selectedTimeSlot.equalsIgnoreCase("") && selectedTimeSlotUpdated.equalsIgnoreCase("")) {
//                        btn_login.setVisibility(View.GONE);
//
//                    }else{
//                        btn_login.setVisibility(View.VISIBLE);
//                    }
                } else {
                    Toaster.customToast(data.getString("error"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            error.printStackTrace();
            loaderView.hideLoader();
            Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                param.put("date_slot", dateGot);
                param.put("time_slot", selectedTimeSlot);
                // param.put("date_time_slot",jsonArray.toString());
                param.put("booking_close_time", "booking_close_time");

                Timber.e(param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    public void getUsersDetails() {
        //loaderView.showLoader();
        StringRequest postRequest = new StringRequest(Request.Method.POST, Global.URL + "get_user_data",
                response -> {
                    Log.d("getUserDetails", response);
                    //loaderView.hideLoader();
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        if (jsonObject.optString("api_text").equalsIgnoreCase("Success")) {
                            JSONObject object = jsonObject.getJSONObject("coach_data");

                            if (object.has("coach_available_date")) {
                                JSONArray jsonObject_personal_info = object.getJSONArray("coach_available_date");
                                setData(jsonObject_personal_info);
                            }

//                                if (object.has("coach_booking_close_time")) {
//
//                                    booking_close_time = object.getString("coach_booking_close_time");
//                                    selectedCloseLimit = "Booking session should be closed before " + booking_close_time + " hours";
//                                    spinerCurency.setText(selectedCloseLimit);
//                                    //Toaster.customToast(closeTime);
//                                }

                        } else if (jsonObject.optString("api_text").equalsIgnoreCase("failed")) {
                            Global.msgDialog(getActivity(), jsonObject.optJSONObject("errors").optString("error_text"));
                        } else {
                            Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        // loaderView.hideLoader();
                        Global.msgDialog(getActivity(), getResources().getString(R.string.error_server));
//                Global.msgDialog(EditProfile.this, "Internet connection is slow");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", SessionManager.get_user_id(prefs));
                param.put("user_profile_id", SessionManager.get_user_id(prefs));
                param.put("s", SessionManager.get_session_id(prefs));
                System.out.println("data   :" + param);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);

    }

    private void setData(JSONArray data) {

        if (data != null) {
            updateJsonArray = data;
            try {
                JSONObject jsonObject = null;
                String avl_date = "";
                JSONObject jsonObjectUp = null;

                ArrayList<String> toArray = new ArrayList<>();
                for (int i = 0; i < data.length(); i++) {
                    jsonObject = data.getJSONObject(i);

                    if (jsonObject.has("available_date")) {
                        avl_date = jsonObject.getString("available_date");
                        updatedDateList.add(avl_date);
                        JSONArray jsonArray = jsonObject.getJSONArray("slot_id");
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                            updatedTimeSlot timeSlot = new updatedTimeSlot(jsonObject1);
                            toArray.add(timeSlot.getSlotId());
                            //...new code should be here...
                        }

                       // Toaster.customToast(updatedDateList.size()+"");

                        String[] newName = toArray.toArray(new String[toArray.size()]);
                        selectedTimeSlotUpdated = toCSV(newName);
//                        jsonObjectUp = new JSONObject();
//                        jsonObjectUp.put(Global.getDateGotCoachh(avl_date), selectedTimeSlot);
//                        jsonArrayUp.put(jsonObjectUp);
//                        updatedDateList.add(avl_date);
                    }
                }

                // Log.d("newS",jsonArrayUp.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }


            year = Global.getYear(fragmentCoachDateSesssionBinding.calendarView.getCurrentPageDate().getTime().toString());
            month = Global.getMonth(fragmentCoachDateSesssionBinding.calendarView.getCurrentPageDate().getTime().toString());
            day = Global.getDay(fragmentCoachDateSesssionBinding.calendarView.getCurrentPageDate().getTime().toString());

            printDatesInMonth(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));

        }

    }

    private void refreshData(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ft.detach(this).commitNow();
            ft.attach(this).commitNow();
        } else {
            ft.detach(this).attach(this).commit();
        }

    }

    public void setSaveButtonVisibility(boolean isVisible) {
        if (fragmentCoachDateSesssionBinding.flSave != null) {
            fragmentCoachDateSesssionBinding.flSave.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

}