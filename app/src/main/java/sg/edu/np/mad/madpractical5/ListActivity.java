package sg.edu.np.mad.madpractical5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Initialize the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        UserAdapter adapter = new UserAdapter(getUsersFromDatabase(), this); // Pass context as second parameter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        // Adding click listener to RecyclerView
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
                builder.setTitle("Profile")
                        .setMessage("MADness")
                        .setPositiveButton("VIEW", (dialog, which) -> {
                            // Generate a random integer
                            Random random = new Random();
                            int randomInt = random.nextInt(100); // Generates a random integer from 0 to 99

                            // Create an Intent to start MainActivity
                            Intent intent = new Intent(ListActivity.this, MainActivity.class);
                            intent.putExtra("RandomNumber", randomInt);
                            startActivity(intent);
                        })
                        .setNegativeButton("CLOSE", (dialog, which) -> dialog.dismiss())
                        .setCancelable(true);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private List<User> getUsersFromDatabase() {
        DBHandler db = DBHandler.getInstance(this);
        return db.getUsers();
    }
}
