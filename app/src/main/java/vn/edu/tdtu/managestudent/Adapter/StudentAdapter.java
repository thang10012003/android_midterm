package vn.edu.tdtu.managestudent.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import vn.edu.tdtu.managestudent.Models.Student;
import vn.edu.tdtu.managestudent.R;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    Context context;
    ArrayList<Student> arrayList;
    FirebaseFirestore fb;
    CollectionReference collectionRef;
    DocumentReference documentReference;

    public StudentAdapter(Context context, ArrayList<Student> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }
    @NonNull
    @Override
    public StudentAdapter.StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StudentViewHolder(LayoutInflater.from(context).inflate(R.layout.student_item, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        //truy cap firestore
        fb = FirebaseFirestore.getInstance();
        collectionRef = fb.collection("Student");



        holder.name.setText(arrayList.get(position).getName());
        holder.MSSV.setText(arrayList.get(position).getId());
        holder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view,holder);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder{
        TextView name,MSSV;
        ImageView option;
        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvName);
            MSSV = itemView.findViewById(R.id.tvMSSV);
            option = itemView.findViewById(R.id.imgOption);
        }
    }
    public void showPopupMenu(View view,@NonNull StudentViewHolder holder){
        PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.deleteStudent){
                    removeItem(holder.getAdapterPosition());
                }
                if(menuItem.getItemId() == R.id.getDetailStudent){

                }
                if(menuItem.getItemId() == R.id.updateStudent){

                }
                return true;
            }
        });
        popupMenu.show();
    }
    public void  removeItem(int pos){
        documentReference = collectionRef.document(arrayList.get(pos).getIdDB());
        documentReference.delete()
                .addOnSuccessListener(aVoid -> {
                    // Xóa thành công
                    Log.d("TAG", "Tài liệu đã được xóa thành công!");
                })
                .addOnFailureListener(e -> {
                    // Xảy ra lỗi trong quá trình xóa
                    Log.w("TAG", "Lỗi khi xóa tài liệu", e);
                });
//        Log.e("TAG", arrayList.get(pos).getIdDB());
//        Log.e("TAG", arrayList.get(pos).getId());
//        Log.e("TAG", "item count" +String.valueOf(getItemCount()));
//        Log.e("TAG", "vi tri" +String.valueOf(pos));
//        Log.e("TAG", "arr size:  " + arrayList.size());
        arrayList.remove((pos));
        notifyItemRemoved(pos);


    }
}
