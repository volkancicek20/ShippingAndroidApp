package com.socksapp.mobileproject.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.socksapp.mobileproject.R;
import com.socksapp.mobileproject.databinding.FragmentAddBinding;
import com.socksapp.mobileproject.databinding.FragmentProfileBinding;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddFragment extends Fragment {

    private FragmentAddBinding binding;
    private String[] cityNames,districtNames,cityNames2,districtNames2,loadTypes,loadAmounts;
    private ArrayAdapter<String> cityAdapter,districtAdapter,cityAdapter2,districtAdapter2,loadAdapter,loadAdapter2;
    private AutoCompleteTextView cityCompleteTextView,districtCompleteTextView,cityCompleteTextView2,districtCompleteTextView2,loadTypeText,loadAmountText;

    public AddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadTypes = getResources().getStringArray(R.array.load_types);
        loadAdapter = new ArrayAdapter<>(requireContext(), R.layout.list_item,loadTypes);
        loadTypeText = binding.getRoot().findViewById(R.id.load_type_text);
        loadTypeText.setAdapter(loadAdapter);

        loadAmounts = getResources().getStringArray(R.array.load_amounts);
        loadAdapter2 = new ArrayAdapter<>(requireContext(), R.layout.list_item,loadAmounts);
        loadAmountText = binding.getRoot().findViewById(R.id.load_amount_text);
        loadAmountText.setAdapter(loadAdapter2);

        cityNames = getResources().getStringArray(R.array.city_names);
        cityAdapter = new ArrayAdapter<>(requireContext(), R.layout.list_item,cityNames);
        cityCompleteTextView = binding.getRoot().findViewById(R.id.city_complete_text);
        cityCompleteTextView.setAdapter(cityAdapter);

        cityNames2 = getResources().getStringArray(R.array.city_names);
        cityAdapter2 = new ArrayAdapter<>(requireContext(), R.layout.list_item,cityNames2);
        cityCompleteTextView2 = binding.getRoot().findViewById(R.id.city_complete_text_2);
        cityCompleteTextView2.setAdapter(cityAdapter2);

        binding.cityCompleteText.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedCity = parent.getItemAtPosition(position).toString();
            binding.districtCompleteText.setText("");
            binding.districtCompleteText.setAdapter(null);
            selectDistrict(selectedCity);
        });

        binding.cityCompleteText2.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedCity = parent.getItemAtPosition(position).toString();
            binding.districtCompleteText2.setText("");
            binding.districtCompleteText2.setAdapter(null);
            selectDistrict2(selectedCity);
        });


        binding.loadInfoTitleConstraintLayout.setOnTouchListener((v, event) -> {
            int checkVisible = binding.visibleLoadInfo.getVisibility();
            if(checkVisible == View.GONE){
                binding.visibleLoadInfo.setVisibility(View.VISIBLE);
            }else {
                binding.visibleLoadInfo.setVisibility(View.GONE);
            }
            return false;
        });

        binding.firstLocationTitleConstraintLayout.setOnTouchListener((v, event) -> {
            int checkVisible = binding.visibleFirstLocation.getVisibility();
            if(checkVisible == View.GONE){
                binding.visibleFirstLocation.setVisibility(View.VISIBLE);
            }else {
                binding.visibleFirstLocation.setVisibility(View.GONE);
            }
            return false;
        });

        binding.secondLocationTitleConstraintLayout.setOnTouchListener((v, event) -> {
            int checkVisible = binding.visibleSecondLocation.getVisibility();
            if(checkVisible == View.GONE){
                binding.visibleSecondLocation.setVisibility(View.VISIBLE);
            }else {
                binding.visibleSecondLocation.setVisibility(View.GONE);
            }
            return false;
        });

        binding.dateTitleConstraintLayout.setOnTouchListener((v, event) -> {
            int checkVisible = binding.visibleDatePicker.getVisibility();
            if(checkVisible == View.GONE){
                binding.visibleDatePicker.setVisibility(View.VISIBLE);
            }else {
                binding.visibleDatePicker.setVisibility(View.GONE);
            }
            return false;
        });

        binding.contactTitleConstraintLayout.setOnTouchListener((v, event) -> {
            int checkVisible = binding.visibleContactPicker.getVisibility();
            if(checkVisible == View.GONE){
                binding.visibleContactPicker.setVisibility(View.VISIBLE);
            }else {
                binding.visibleContactPicker.setVisibility(View.GONE);
            }
            return false;
        });

        binding.checkBoxConcact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.mailTextInputLayout.setVisibility(View.GONE);
                    binding.numberTextInputLayout.setVisibility(View.GONE);
                } else {
                    binding.mailTextInputLayout.setVisibility(View.VISIBLE);
                    binding.numberTextInputLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.openDate.setOnClickListener(this::showCustomDateDialog);

        binding.openTime.setOnClickListener(this::showCustomTimeDialog);

    }

    private void showCustomTimeDialog(View view) {
        Dialog dialog = new Dialog(view.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.start_and_end_time_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

//        Button first = dialog.findViewById(R.id.firstTime);
//        Button second = dialog.findViewById(R.id.secondTime);
        Button save = dialog.findViewById(R.id.saveTime);
//        TextView firstText = dialog.findViewById(R.id.firstTimeText);
//        TextView secondText = dialog.findViewById(R.id.secondTimeText);

//
//        Calendar calendar = Calendar.getInstance();
//        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
//        int minute = calendar.get(Calendar.MINUTE);
//
//        first.setOnClickListener(v ->{
//            TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
//                @Override
//                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                    @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d", hourOfDay, minute);
//                    firstText.setText(time);
//                }
//            }, hourOfDay, minute, true);
//            timePickerDialog.show();
//        });
//
//        second.setOnClickListener(v ->{
//            TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
//                @Override
//                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                    @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d", hourOfDay, minute);
//                    secondText.setText(time);
//                }
//            }, hourOfDay, minute, true);
//            timePickerDialog.show();
//        });

        save.setOnClickListener(v ->{
//            String firstTime = firstText.getText().toString();
//            String secondTime = secondText.getText().toString();

//            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//            try {
//                Date date1 = sdf.parse(firstTime);
//                Date date2 = sdf.parse(secondTime);
//                int comparisonResult = date1.compareTo(date2);
//
//                if (comparisonResult < 0) {
//                    // time1, time2'den önce
//                    String time = firstTime + " - " + secondTime;
//
//                    binding.timeText.setText(time);
//                    dialog.dismiss();
//                } else if (comparisonResult > 0) {
//                    Toast.makeText(v.getContext(),"Bitiş saati başlangıç saatinden önce olamaz!",Toast.LENGTH_LONG).show();
//                } else {
//                    // time1 ve time2 aynı saate sahip
//                    String time = firstTime + " - " + secondTime;
//
//                    binding.timeText.setText(time);
//                    dialog.dismiss();
//                }
//            } catch (ParseException e) {
//                throw new RuntimeException(e);
//            }
        });

        dialog.show();
    }

    private void showCustomDateDialog(View view) {
        Dialog dialog = new Dialog(view.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.start_and_end_date_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

//        Button first = dialog.findViewById(R.id.firstDate);
//        Button second = dialog.findViewById(R.id.secondDate);
        Button save = dialog.findViewById(R.id.saveDate);
//        TextView firstText = dialog.findViewById(R.id.firstDateText);
//        TextView secondText = dialog.findViewById(R.id.secondDateText);

//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
//
//        first.setOnClickListener(v ->{
//            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
//                    new DatePickerDialog.OnDateSetListener() {
//                        @Override
//                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                            String monthName = new DateFormatSymbols(new Locale("tr")).getMonths()[month];
//                            String selectedDate = dayOfMonth + " " + monthName + " " + year;
//                            firstText.setText(selectedDate);
//                        }
//                    }, year, month, dayOfMonth);
//
//            datePickerDialog.show();
//        });
//
//        second.setOnClickListener(v ->{
//            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
//                    new DatePickerDialog.OnDateSetListener() {
//                        @Override
//                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                            String monthName = new DateFormatSymbols(new Locale("tr")).getMonths()[month];
//                            String selectedDate = dayOfMonth + " " + monthName + " " + year;
//                            secondText.setText(selectedDate);
//                        }
//                    }, year, month, dayOfMonth);
//
//            datePickerDialog.show();
//        });

        save.setOnClickListener(v ->{
//            String firstDate = firstText.getText().toString();
//            String secondDate = secondText.getText().toString();

//            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("tr"));
//            try {
//                Date date1 = sdf.parse(firstDate);
//                Date date2 = sdf.parse(secondDate);
//                int comparisonResult = date1.compareTo(date2);
//
//                if (comparisonResult < 0) {
//                    String dateRange = firstDate + " - " + secondDate;
//                    binding.dateText.setText(dateRange);
//                    dialog.dismiss();
//                } else if (comparisonResult > 0) {
//                    Toast.makeText(v.getContext(), "Bitiş tarihi başlangıç tarihinden önce olamaz!", Toast.LENGTH_LONG).show();
//                } else {
//                    String dateRange = firstDate + " - " + secondDate;
//                    binding.dateText.setText(dateRange);
//                    dialog.dismiss();
//                }
//            } catch (ParseException e) {
//                throw new RuntimeException(e);
//            }
        });

        dialog.show();
    }

    private void selectDistrict(String selectedCity){
        switch (selectedCity){
            case "İstanbul":
                districtNames = getResources().getStringArray(R.array.district_istanbul);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Ankara":
                districtNames = getResources().getStringArray(R.array.district_ankara);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "İzmir":
                districtNames = getResources().getStringArray(R.array.district_izmir);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Adana":
                districtNames = getResources().getStringArray(R.array.district_adana);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Adıyaman":
                districtNames = getResources().getStringArray(R.array.district_adiyaman);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Afyonkarahisar":
                districtNames = getResources().getStringArray(R.array.district_afyonkarahisar);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Ağrı":
                districtNames = getResources().getStringArray(R.array.district_agri);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Amasya":
                districtNames = getResources().getStringArray(R.array.district_amasya);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Antalya":
                districtNames = getResources().getStringArray(R.array.district_antalya);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Artvin":
                districtNames = getResources().getStringArray(R.array.district_artvin);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Aydın":
                districtNames = getResources().getStringArray(R.array.district_aydin);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Balıkesir":
                districtNames = getResources().getStringArray(R.array.district_balikesir);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Bilecik":
                districtNames = getResources().getStringArray(R.array.district_bilecik);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Bingöl":
                districtNames = getResources().getStringArray(R.array.district_bingol);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Bitlis":
                districtNames = getResources().getStringArray(R.array.district_bitlis);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Bolu":
                districtNames = getResources().getStringArray(R.array.district_bolu);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Burdur":
                districtNames = getResources().getStringArray(R.array.district_burdur);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Bursa":
                districtNames = getResources().getStringArray(R.array.district_bursa);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Çanakkale":
                districtNames = getResources().getStringArray(R.array.district_canakkale);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Çankırı":
                districtNames = getResources().getStringArray(R.array.district_cankiri);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Çorum":
                districtNames = getResources().getStringArray(R.array.district_corum);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Denizli":
                districtNames = getResources().getStringArray(R.array.district_denizli);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Diyarbakır":
                districtNames = getResources().getStringArray(R.array.district_diyarbakir);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Edirne":
                districtNames = getResources().getStringArray(R.array.district_edirne);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Elazığ":
                districtNames = getResources().getStringArray(R.array.district_elazig);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Erzincan":
                districtNames = getResources().getStringArray(R.array.district_erzincan);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Erzurum":
                districtNames = getResources().getStringArray(R.array.district_erzurum);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Eskişehir":
                districtNames = getResources().getStringArray(R.array.district_eskisehir);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Gaziantep":
                districtNames = getResources().getStringArray(R.array.district_gaziantep);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Giresun":
                districtNames = getResources().getStringArray(R.array.district_giresun);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Gümüşhane":
                districtNames = getResources().getStringArray(R.array.district_gumushane);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Hakkari":
                districtNames = getResources().getStringArray(R.array.district_hakkari);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Hatay":
                districtNames = getResources().getStringArray(R.array.district_hatay);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Isparta":
                districtNames = getResources().getStringArray(R.array.district_isparta);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Mersin":
                districtNames = getResources().getStringArray(R.array.district_mersin);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Kars":
                districtNames = getResources().getStringArray(R.array.district_kars);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Kastamonu":
                districtNames = getResources().getStringArray(R.array.district_kastamonu);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Kayseri":
                districtNames = getResources().getStringArray(R.array.district_kayseri);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Kırklareli":
                districtNames = getResources().getStringArray(R.array.district_kirklareli);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Kırşehir":
                districtNames = getResources().getStringArray(R.array.district_kirsehir);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Kocaeli":
                districtNames = getResources().getStringArray(R.array.district_kocaeli);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Konya":
                districtNames = getResources().getStringArray(R.array.district_konya);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Kütahya":
                districtNames = getResources().getStringArray(R.array.district_kutahya);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Malatya":
                districtNames = getResources().getStringArray(R.array.district_malatya);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Manisa":
                districtNames = getResources().getStringArray(R.array.district_manisa);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Kahramanmaraş":
                districtNames = getResources().getStringArray(R.array.district_kahramanmaras);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Mardin":
                districtNames = getResources().getStringArray(R.array.district_mardin);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Muğla":
                districtNames = getResources().getStringArray(R.array.district_mugla);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Muş":
                districtNames = getResources().getStringArray(R.array.district_mus);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Nevşehir":
                districtNames = getResources().getStringArray(R.array.district_nevsehir);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Niğde":
                districtNames = getResources().getStringArray(R.array.district_nigde);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Ordu":
                districtNames = getResources().getStringArray(R.array.district_ordu);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Rize":
                districtNames = getResources().getStringArray(R.array.district_rize);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Sakarya":
                districtNames = getResources().getStringArray(R.array.district_sakarya);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Samsun":
                districtNames = getResources().getStringArray(R.array.district_samsun);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Siirt":
                districtNames = getResources().getStringArray(R.array.district_siirt);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Sinop":
                districtNames = getResources().getStringArray(R.array.district_sinop);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Sivas":
                districtNames = getResources().getStringArray(R.array.district_sivas);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Tekirdağ":
                districtNames = getResources().getStringArray(R.array.district_tekirdag);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Tokat":
                districtNames = getResources().getStringArray(R.array.district_tokat);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Trabzon":
                districtNames = getResources().getStringArray(R.array.district_trabzon);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Tunceli":
                districtNames = getResources().getStringArray(R.array.district_tunceli);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Şanlıurfa":
                districtNames = getResources().getStringArray(R.array.district_sanliurfa);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Uşak":
                districtNames = getResources().getStringArray(R.array.district_usak);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Van":
                districtNames = getResources().getStringArray(R.array.district_van);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Yozgat":
                districtNames = getResources().getStringArray(R.array.district_yozgat);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Zonguldak":
                districtNames = getResources().getStringArray(R.array.district_zonguldak);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Aksaray":
                districtNames = getResources().getStringArray(R.array.district_aksaray);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Bayburt":
                districtNames = getResources().getStringArray(R.array.district_bayburt);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Karaman":
                districtNames = getResources().getStringArray(R.array.district_karaman);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Kırıkkale":
                districtNames = getResources().getStringArray(R.array.district_kirikkale);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Batman":
                districtNames = getResources().getStringArray(R.array.district_batman);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Şırnak":
                districtNames = getResources().getStringArray(R.array.district_sirnak);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Bartın":
                districtNames = getResources().getStringArray(R.array.district_bartin);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Ardahan":
                districtNames = getResources().getStringArray(R.array.district_ardahan);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Iğdır":
                districtNames = getResources().getStringArray(R.array.district_igdir);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Yalova":
                districtNames = getResources().getStringArray(R.array.district_yalova);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Karabük":
                districtNames = getResources().getStringArray(R.array.district_karabuk);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Kilis":
                districtNames = getResources().getStringArray(R.array.district_kilis);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Osmaniye":
                districtNames = getResources().getStringArray(R.array.district_osmaniye);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            case "Düzce":
                districtNames = getResources().getStringArray(R.array.district_duzce);
                districtAdapter = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames);
                districtCompleteTextView = binding.getRoot().findViewById(R.id.district_complete_text);
                districtCompleteTextView.setAdapter(districtAdapter);
                break;
            default:
                break;
        }
    }
    private void selectDistrict2(String selectedCity){
        switch (selectedCity){
            case "İstanbul":
                districtNames2 = getResources().getStringArray(R.array.district_istanbul);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Ankara":
                districtNames2 = getResources().getStringArray(R.array.district_ankara);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "İzmir":
                districtNames2 = getResources().getStringArray(R.array.district_izmir);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Adana":
                districtNames2 = getResources().getStringArray(R.array.district_adana);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Adıyaman":
                districtNames2 = getResources().getStringArray(R.array.district_adiyaman);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Afyonkarahisar":
                districtNames2 = getResources().getStringArray(R.array.district_afyonkarahisar);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Ağrı":
                districtNames2 = getResources().getStringArray(R.array.district_agri);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Amasya":
                districtNames2 = getResources().getStringArray(R.array.district_amasya);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Antalya":
                districtNames2 = getResources().getStringArray(R.array.district_antalya);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Artvin":
                districtNames2 = getResources().getStringArray(R.array.district_artvin);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Aydın":
                districtNames2 = getResources().getStringArray(R.array.district_aydin);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Balıkesir":
                districtNames2 = getResources().getStringArray(R.array.district_balikesir);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Bilecik":
                districtNames2 = getResources().getStringArray(R.array.district_bilecik);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Bingöl":
                districtNames2 = getResources().getStringArray(R.array.district_bingol);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Bitlis":
                districtNames2 = getResources().getStringArray(R.array.district_bitlis);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Bolu":
                districtNames2 = getResources().getStringArray(R.array.district_bolu);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Burdur":
                districtNames2 = getResources().getStringArray(R.array.district_burdur);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Bursa":
                districtNames2 = getResources().getStringArray(R.array.district_bursa);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Çanakkale":
                districtNames2 = getResources().getStringArray(R.array.district_canakkale);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Çankırı":
                districtNames2 = getResources().getStringArray(R.array.district_cankiri);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Çorum":
                districtNames2 = getResources().getStringArray(R.array.district_corum);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Denizli":
                districtNames2 = getResources().getStringArray(R.array.district_denizli);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Diyarbakır":
                districtNames2 = getResources().getStringArray(R.array.district_diyarbakir);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Edirne":
                districtNames2 = getResources().getStringArray(R.array.district_edirne);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Elazığ":
                districtNames2 = getResources().getStringArray(R.array.district_elazig);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Erzincan":
                districtNames2 = getResources().getStringArray(R.array.district_erzincan);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Erzurum":
                districtNames2 = getResources().getStringArray(R.array.district_erzurum);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Eskişehir":
                districtNames2 = getResources().getStringArray(R.array.district_eskisehir);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Gaziantep":
                districtNames2 = getResources().getStringArray(R.array.district_gaziantep);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Giresun":
                districtNames2 = getResources().getStringArray(R.array.district_giresun);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Gümüşhane":
                districtNames2 = getResources().getStringArray(R.array.district_gumushane);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Hakkari":
                districtNames2 = getResources().getStringArray(R.array.district_hakkari);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Hatay":
                districtNames2 = getResources().getStringArray(R.array.district_hatay);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Isparta":
                districtNames2 = getResources().getStringArray(R.array.district_isparta);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Mersin":
                districtNames2 = getResources().getStringArray(R.array.district_mersin);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Kars":
                districtNames2 = getResources().getStringArray(R.array.district_kars);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Kastamonu":
                districtNames2 = getResources().getStringArray(R.array.district_kastamonu);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Kayseri":
                districtNames2 = getResources().getStringArray(R.array.district_kayseri);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Kırklareli":
                districtNames2 = getResources().getStringArray(R.array.district_kirklareli);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Kırşehir":
                districtNames2 = getResources().getStringArray(R.array.district_kirsehir);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Kocaeli":
                districtNames2 = getResources().getStringArray(R.array.district_kocaeli);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Konya":
                districtNames2 = getResources().getStringArray(R.array.district_konya);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Kütahya":
                districtNames2 = getResources().getStringArray(R.array.district_kutahya);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Malatya":
                districtNames2 = getResources().getStringArray(R.array.district_malatya);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Manisa":
                districtNames2 = getResources().getStringArray(R.array.district_manisa);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Kahramanmaraş":
                districtNames2 = getResources().getStringArray(R.array.district_kahramanmaras);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Mardin":
                districtNames2 = getResources().getStringArray(R.array.district_mardin);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Muğla":
                districtNames2 = getResources().getStringArray(R.array.district_mugla);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Muş":
                districtNames2 = getResources().getStringArray(R.array.district_mus);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Nevşehir":
                districtNames2 = getResources().getStringArray(R.array.district_nevsehir);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Niğde":
                districtNames2 = getResources().getStringArray(R.array.district_nigde);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Ordu":
                districtNames2 = getResources().getStringArray(R.array.district_ordu);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Rize":
                districtNames2 = getResources().getStringArray(R.array.district_rize);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Sakarya":
                districtNames2 = getResources().getStringArray(R.array.district_sakarya);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Samsun":
                districtNames2 = getResources().getStringArray(R.array.district_samsun);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Siirt":
                districtNames2 = getResources().getStringArray(R.array.district_siirt);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Sinop":
                districtNames2 = getResources().getStringArray(R.array.district_sinop);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Sivas":
                districtNames2 = getResources().getStringArray(R.array.district_sivas);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Tekirdağ":
                districtNames2 = getResources().getStringArray(R.array.district_tekirdag);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Tokat":
                districtNames2 = getResources().getStringArray(R.array.district_tokat);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Trabzon":
                districtNames2 = getResources().getStringArray(R.array.district_trabzon);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Tunceli":
                districtNames2 = getResources().getStringArray(R.array.district_tunceli);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Şanlıurfa":
                districtNames2 = getResources().getStringArray(R.array.district_sanliurfa);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Uşak":
                districtNames2 = getResources().getStringArray(R.array.district_usak);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Van":
                districtNames2 = getResources().getStringArray(R.array.district_van);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Yozgat":
                districtNames2 = getResources().getStringArray(R.array.district_yozgat);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Zonguldak":
                districtNames2 = getResources().getStringArray(R.array.district_zonguldak);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Aksaray":
                districtNames2 = getResources().getStringArray(R.array.district_aksaray);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Bayburt":
                districtNames2 = getResources().getStringArray(R.array.district_bayburt);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Karaman":
                districtNames2 = getResources().getStringArray(R.array.district_karaman);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Kırıkkale":
                districtNames2 = getResources().getStringArray(R.array.district_kirikkale);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Batman":
                districtNames2 = getResources().getStringArray(R.array.district_batman);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Şırnak":
                districtNames2 = getResources().getStringArray(R.array.district_sirnak);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Bartın":
                districtNames2 = getResources().getStringArray(R.array.district_bartin);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Ardahan":
                districtNames2 = getResources().getStringArray(R.array.district_ardahan);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Iğdır":
                districtNames2 = getResources().getStringArray(R.array.district_igdir);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Yalova":
                districtNames2 = getResources().getStringArray(R.array.district_yalova);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Karabük":
                districtNames2 = getResources().getStringArray(R.array.district_karabuk);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Kilis":
                districtNames2 = getResources().getStringArray(R.array.district_kilis);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Osmaniye":
                districtNames2 = getResources().getStringArray(R.array.district_osmaniye);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            case "Düzce":
                districtNames2 = getResources().getStringArray(R.array.district_duzce);
                districtAdapter2 = new ArrayAdapter<>(requireContext(),R.layout.list_item,districtNames2);
                districtCompleteTextView2 = binding.getRoot().findViewById(R.id.district_complete_text_2);
                districtCompleteTextView2.setAdapter(districtAdapter2);
                break;
            default:
                break;
        }
    }
}