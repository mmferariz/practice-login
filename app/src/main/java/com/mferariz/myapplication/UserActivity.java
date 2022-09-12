package com.mferariz.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.mferariz.myapplication.ui.utils.CustomDialog;
import com.mferariz.myapplication.controller.UserController;
import com.mferariz.myapplication.databinding.ActivityUserBinding;
import com.mferariz.myapplication.model.SingleUserResponse;
import com.mferariz.myapplication.model.User;
import com.mferariz.myapplication.ui.utils.SnackbarType;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {

    private ActivityUserBinding binding;
    private final UserController userController = new UserController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int id = getIntent().getIntExtra("id", 0);
        if(id == 0){
            CustomDialog.showSnackbar("Id de usuario incorrecto, regrese a la pantalla anterior", binding.getRoot());
        } else {
            getUser(id);
        }
    }

    private void getUser(int id){

        CustomDialog dialog = new CustomDialog(this);
        dialog.startLoadingDialog();
        userController.getUser(id).enqueue(new Callback<SingleUserResponse>() {
            @Override
            public void onResponse(Call<SingleUserResponse> call, Response<SingleUserResponse> response) {
                if(response.isSuccessful()){
                    User user = response.body().getUser();
                    binding.txtId.setText("ID: " + user.getId());
                    binding.txtName.setText("Nombre: "+ user.getFirstName() + " " + user.getLastName());
                    binding.txtEmail.setText("Correo: " + user.getEmail());
                    Picasso.get().load(user.getAvatar()).error(R.mipmap.ic_error_avatar).into(binding.avatar);
                }
                dialog.dismissDialog();
            }

            @Override
            public void onFailure(Call<SingleUserResponse> call, Throwable t) {
                Log.d("getUsers:", t.toString());
                dialog.dismissDialog();
                CustomDialog.showSnackbar(t.toString(), binding.getRoot(), SnackbarType.ERROR);
            }
        });
    }
}