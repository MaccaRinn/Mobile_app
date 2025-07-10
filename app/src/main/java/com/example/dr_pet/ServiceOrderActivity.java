package com.example.dr_pet;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.widget.EditText;
import java.util.Calendar;

public class ServiceOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_service_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView txtDetail = findViewById(R.id.textView10);
        String detail = getIntent().getStringExtra("service_detail");
        txtDetail.setText(detail);

        ImageView imgService = findViewById(R.id.imgServiceOrder);
        TextView txtName = findViewById(R.id.txtServiceOrderName);
        TextView txtPrice = findViewById(R.id.txtServiceOrderPrice);

        String name = getIntent().getStringExtra("service_name");
        int price = getIntent().getIntExtra("service_price", 0);
        int imgRes = getIntent().getIntExtra("service_img", 0);

        txtName.setText(name);
        txtPrice.setText(price + " VNĐ");
        imgService.setImageResource(imgRes);

        // Lấy ngày đặt từ DatePicker khi xác nhận
        EditText edtOrderDate = findViewById(R.id.edtOrderDate);
        edtOrderDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String date = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                        edtOrderDate.setText(date);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        // Khi xác nhận đặt dịch vụ, lấy ngày đã chọn:
        Button btnConfirm = findViewById(R.id.btnConfirmOrder);
        btnConfirm.setOnClickListener(v -> {
            String date = edtOrderDate.getText().toString();
            // ... xử lý tiếp ...
        });
    }
    }
