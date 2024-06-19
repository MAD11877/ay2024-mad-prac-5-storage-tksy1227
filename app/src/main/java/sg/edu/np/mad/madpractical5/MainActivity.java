package sg.edu.np.mad.madpractical5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;
import java.util.Random;
public class MainActivity extends AppCompatActivity {

    private DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        DBHandler db = DBHandler.getInstance(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve the random integer from the Intent
        int randomInt = getIntent().getIntExtra("RandomNumber", 0); // Set to 0 if no data

        // Getting references to the UI components
        TextView tvName = findViewById(R.id.tvName);
        TextView tvDescription = findViewById(R.id.tvDescription);
        Button btnFollow = findViewById(R.id.btnFollow);

        // Get a random user from the database
        List<User> userList = db.getUsers();
        User user = userList.get(randomInt % userList.size()); // Ensure index is within bounds

        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check current text of the button
                if (btnFollow.getText().toString().equalsIgnoreCase("FOLLOW")) {
                    Toast.makeText(MainActivity.this, "Followed", Toast.LENGTH_SHORT).show();
                    btnFollow.setText("UNFOLLOW");
                    user.setFollowed(!user.isFollowed()); // Toggle the followed status
                    // Update the database
                    db.updateUser(user);
                } else {
                    Toast.makeText(MainActivity.this, "Unfollowed", Toast.LENGTH_SHORT).show();
                    btnFollow.setText("FOLLOW");
                    user.setFollowed(!user.isFollowed()); // Toggle the unfollowed status
                    // Update the database
                    db.updateUser(user);
                }
            }
        });

        // Set the TextViews with the User's name and description
        tvName.setText(user.getName());
        tvDescription.setText(user.getDescription());
        btnFollow.setText(user.isFollowed() ? "UNFOLLOW" : "FOLLOW");  // Set the initial button text based on user's followed status
    }

    @Override
    protected void onDestroy() {
        db.close(); // Close the database connection when the activity is destroyed
        super.onDestroy();
    }
}
