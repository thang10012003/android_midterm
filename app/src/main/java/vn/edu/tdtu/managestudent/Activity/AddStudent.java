package vn.edu.tdtu.managestudent.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.takisoft.datetimepicker.DatePickerDialog;
import com.takisoft.datetimepicker.widget.DatePicker;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import vn.edu.tdtu.managestudent.Models.Student;
import vn.edu.tdtu.managestudent.databinding.ActivityMainBinding;

public class AddStudent extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseFirestore fb;
    CollectionReference collectionRef;
    Student student;
    String id = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fb = FirebaseFirestore.getInstance();
        collectionRef = fb.collection("Student");
        collectionRef.orderBy("MSSV", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            id = document.get("MSSV").toString();
                            if (id != null) {
                                Log.d("TAG", "ID lớn nhất: " + id.toString());
                            } else {
                                Log.d("TAG", "Không có tài liệu nào có trường ID");
                            }
                        }
                    } else {
                        Log.d("TAG", "Lỗi khi truy vấn tài liệu: ", task.getException());
                    }
                });
        binding.btnBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerDialog();
            }
        });

        binding.tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.edtName.getText().toString();
                String birth = binding.edtBirth.getText().toString();
                String gender = binding.maleCb.isChecked()?binding.maleCb.getText().toString():binding.femaleCb.getText().toString();
                String address = binding.edtAddress.getText().toString();
                String citizenID = binding.edtCitizenID.getText().toString();
                Boolean validate = validate(name, gender, birth, address, citizenID);
                Log.d("TAG", "onClick   name: " + validate);
                if(validate){
                    collectionRef.orderBy("MSSV", Query.Direction.DESCENDING)
                            .limit(1)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        id = document.get("MSSV").toString();
                                        if (id != null) {
                                            Log.d("TAG", "ID lớn nhất: " + id.toString());
                                        } else {
                                            Log.d("TAG", "Không có tài liệu nào có trường ID");
                                        }
                                    }
                                } else {
                                    Log.d("TAG", "Lỗi khi truy vấn tài liệu: ", task.getException());
                                }
                            });
                    id = String.valueOf(Integer.parseInt(id)+ 1);

                    Map<String,Object> student = addStudent(id,name, gender, birth, address, citizenID);
//                student = new Student(id,name, gender, birth, address, citizenID);
                    collectionRef
                            .add(student)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(getApplicationContext(), "Thêm thành công",Toast.LENGTH_LONG).show();
                                    onBackPressed();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Thêm thất bại",Toast.LENGTH_LONG).show();
                                }
                            });
                }

            }
        });
    }
    private void openDatePickerDialog() {
        // Lấy ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(Calendar.YEAR, year);
                        selectedDate.set(Calendar.MONTH, monthOfYear);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        if(calendar.compareTo(selectedDate)<0){
                            binding.edtBirth.setError("Ngay khong hop le");
                        }else {
                            binding.edtBirth.setText(dayOfMonth + "/" + (monthOfYear +1)  + "/" + year);
                        }
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }
    private Map<String,Object> addStudent(String id, String name, String gender, String birth, String address, String citizenID){
        Map<String, Object> student = new HashMap<>();
        student.put("MSSV",id);
        student.put("Tên",name);
        student.put("Giới tính",gender);
        student.put("Ngày sinh",birth);
        student.put("Địa chỉ",address);
        student.put("Số CCCD",citizenID);
        return student;
    }

    private boolean validate( String name, String gender, String birth, String address, String citizenID){
        if(name.equals("")){
            binding.edtName.setError("Vui long nhap ten!");
            return false;
        }
        if(gender.equals("")){
            binding.tvGender.setError("Vui long chon gioi tinh!");
            return false;
        }
        if(birth.equals("")){
            binding.edtBirth.setError("Vui long chon ngay sinh!");
            return false;
        }
        if(address.equals("")){
            binding.edtAddress.setError("Vui long nhap dia chi!");
            return false;
        }
        if(citizenID.equals("")){
            binding.edtCitizenID.setError("Vui long nhap so can cuoc!");
            return false;
        }

        return  true;
    }

}
