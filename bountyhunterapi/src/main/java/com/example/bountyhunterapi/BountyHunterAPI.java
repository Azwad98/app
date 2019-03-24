package com.example.bountyhunterapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BountyHunterAPI {
    private Context context;
    private SharedPreferences preferences;


    public BountyHunterAPI(Context context) {
        this.context = context;
    }

    public void registerUser(User user) {
        RetrofitServices service = RetrofitClientInstance.getRetrofitInstance(context).create(RetrofitServices.class);
        Call<Void> call = service.registerUser(user);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 201) {
                    Toast.makeText(context, "Your account was successfully registered", Toast.LENGTH_LONG).show();
                    return;
                } else if (response.code() == 500) {
                    Toast.makeText(context, "An error occurred when trying to register your account \n Please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(context, "Your device is not connected to the internet \n Ensure the device is connected to the internet then try again", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Failed to connect to the server \n please close the application and try again", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    public User loginUser(String username, String password) {
        final RetrofitServices service = RetrofitClientInstance.getRetrofitInstance(context).create(RetrofitServices.class);
        Call<Token> call = service.loginUser(username, password);

        final User[] loggedInUser = new User[1];

        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.code() == 200) {
                    preferences = PreferenceManager.getDefaultSharedPreferences(context);
                    preferences.edit().putString("TOKEN", "Bearer " + response.body().getToken()).apply();
                    loggedInUser[0] = getLoggedInUser(preferences.getString("TOKEN", null));
                } else if (response.code() == 401) {
                    Toast.makeText(context, "The password you entered was incorrect", Toast.LENGTH_LONG).show();
                } else if (response.code() == 404) {
                    Toast.makeText(context, "The password you entered was incorrect", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(context, "Your device is not connected to the internet \n Ensure the device is connected to the internet then try again", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Failed to connect to the server \n please close the application and try again", Toast.LENGTH_LONG).show();

                }
            }
        });
        return loggedInUser[0];
    }

    public User getLoggedInUser(String token) {
        RetrofitServices service = RetrofitClientInstance.getRetrofitInstance(context).create(RetrofitServices.class);
        Call<User> call = service.getLoggedInUser(token);

        final User[] retrievedUser = new User[1];

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    //retrievedUser[0] = new User(response.body().getId(), response.body().getFirstName(), response.body().getLastName(), response.body().getUsername(), response.body().getPassword(), response.body().getEmail(), response.body().isActive(), response.body().isVerified(), response.body().getCreated_at(), response.body().getUpdated_at());
                    retrievedUser[0] = response.body();
                    Toast.makeText(context, retrievedUser[0].getFirstName(), Toast.LENGTH_LONG).show();
//                    Log.d("Response", response.raw().toString());
//                    Log.d("Response", String.valueOf(response.body().getId()));
                } else if (response.code() == 404) {
                    Toast.makeText(context, "Could not find the user account with the specified username \n Please try again", Toast.LENGTH_LONG).show();
                } else if (response.code() == 401) {
                    Toast.makeText(context, "Authorization failed \n Please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(context, "Your device is not connected to the internet \n Ensure the device is connected to the internet then try again", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Failed to connect to the server \n please close the application and try again", Toast.LENGTH_LONG).show();

                }
            }
        });
        return retrievedUser[0];
    }

    public User getUser(int userID) {
        RetrofitServices service = RetrofitClientInstance.getRetrofitInstance(context).create(RetrofitServices.class);

        final User[] retrievedUser = new User[1];

        String token = preferences.getString("TOKEN", null);
        Call<User> call = service.getUser(token, userID);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    Toast.makeText(context, "User account was found", Toast.LENGTH_LONG).show();
                    retrievedUser[0] = new User(response.body().getId(), response.body().getFirstName(), response.body().getLastName(), response.body().getUsername(), response.body().getPassword(), response.body().getEmail(), response.body().isActive(), response.body().isVerified(), response.body().getCreated_at(), response.body().getUpdated_at());
                    return;
                } else if (response.code() == 404) {
                    Toast.makeText(context, "Could not find the user account with the specified username \n Please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(context, "Your device is not connected to the internet \n Ensure the device is connected to the internet then try again", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Failed to connect to the server \n please close the application and try again", Toast.LENGTH_LONG).show();

                }
            }
        });

        return retrievedUser[0];
    }

    public void updateUser(int userID, User updateUser) {
        RetrofitServices service = RetrofitClientInstance.getRetrofitInstance(context).create(RetrofitServices.class);

        String token = preferences.getString("TOKEN", null);
        Call<User> call = service.updateUser(token, userID, updateUser);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(context, "Your device is not connected to the internet \n Ensure the device is connected to the internet then try again", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Failed to connect to the server \n please close the application and try again", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    public void deleteUser(int userID) {

        RetrofitServices service = RetrofitClientInstance.getRetrofitInstance(context).create(RetrofitServices.class);

        String token = ""; //preferences.getString("TOKEN",null);
        Call<Void> call = service.deleteUser(token, userID);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 204) {
                    Toast.makeText(context, "Your account was successfully deleted", Toast.LENGTH_LONG).show();
                    return;
                } else if (response.code() == 500) {
                    Toast.makeText(context, "An error occurred when trying to delete account \n Please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(context, "Your device is not connected to the internet \n Ensure the device is connected to the internet then try again", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Failed to connect to the server \n please close the application and try again", Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    public void searchUser(String username) {
        RetrofitServices service = RetrofitClientInstance.getRetrofitInstance(context).create(RetrofitServices.class);

        String token = ""; //preferences.getString("TOKEN",null);
        Call<UserList> call = service.searchUser(token, username);

        call.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(Call<UserList> call, Response<UserList> response) {
                if (response.code() == 200) {
                    Toast.makeText(context, "User account was found", Toast.LENGTH_LONG).show();
                    return;
                } else if (response.code() == 404) {
                    Toast.makeText(context, "Could not find the user account with the specified username \n Please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserList> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(context, "Your device is not connected to the internet \n Ensure the device is connected to the internet then try again", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Failed to connect to the server \n please close the application and try again", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    public void resetPassoword(int userID) {
        RetrofitServices service = RetrofitClientInstance.getRetrofitInstance(context).create(RetrofitServices.class);

        String token = ""; //preferences.getString("TOKEN",null);
        service.resetPassword(token, userID);
    }

    ;
}
