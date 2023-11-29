package vn.edu.tdtu.managestudent.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import vn.edu.tdtu.managestudent.Adapter.StudentAdapter;
import vn.edu.tdtu.managestudent.Models.Student;
import vn.edu.tdtu.managestudent.R;
import vn.edu.tdtu.managestudent.databinding.ActivityListStudentBinding;

public class ListStudent extends AppCompatActivity {
    ActivityListStudentBinding binding;
    FirebaseFirestore fb;
    CollectionReference collectionRef;
    Student student;
    ArrayList<Student> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fb = FirebaseFirestore.getInstance();
        collectionRef = fb.collection("Student");

        arrayList = new ArrayList<>();

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListStudent.this,AddStudent.class);
                startActivity(intent);
            }
        });
        collectionRef.orderBy("MSSV", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    arrayList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String id = document.getString("MSSV");
                        String name = document.getString("Tên");
                        String gender = document.getString("Giới tính");
                        String birth = document.getString("Ngày sinh");
                        String address = document.getString("Địa chỉ");
                        String citizenID = document.getString("Số CCCD");
                        String idDB = document.getId();
                        Student student = new Student(id,name, gender, birth, address, citizenID,idDB);
                        arrayList.add(student);
                        Log.d("TAG", document.getId() + " => " + document.getData());
                    }
                    Log.d("TAG", "onCreate: " + arrayList.get(0).getName());
                    StudentAdapter adapter = new StudentAdapter(ListStudent.this, arrayList);
                    binding.recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    binding.recycler.setAdapter(adapter);
                }
            }
        });
        binding.tvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collectionRef.orderBy("MSSV", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            arrayList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getString("MSSV");
                                String name = document.getString("Tên");
                                String gender = document.getString("Giới tính");
                                String birth = document.getString("Ngày sinh");
                                String address = document.getString("Địa chỉ");
                                String citizenID = document.getString("Số CCCD");
                                String idDB = document.getId();
                                Student student = new Student(id,name, gender, birth, address, citizenID,idDB);
                                arrayList.add(student);
                                Log.d("TAG", document.getId() + " => " + document.getData());
                            }
                            Log.d("TAG", "onCreate: " + arrayList.get(0).getName());
                            StudentAdapter adapter = new StudentAdapter(ListStudent.this, arrayList);
                            binding.recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            binding.recycler.setAdapter(adapter);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.popup_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return super.onContextItemSelected(item);
    }
}