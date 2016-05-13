package sk.sustr.michal.sayhello;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.MutableDateTime;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    private static final String TAG = "sayhello";
    private ListView mListToday;
    private ListView mListOther;
    private MyListAdapter mToday;
    private MyListAdapter mOther;
    private FriendSqliteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListToday = (ListView) findViewById(R.id.listToday);
        mListOther = (ListView) findViewById(R.id.listOther);

        mToday = new MyListAdapter(getApplicationContext(), new ArrayList<Friend>());
        mOther = new MyListAdapter(getApplicationContext(), new ArrayList<Friend>());

        mListToday.setAdapter(mToday);
        mListOther.setAdapter(mOther);
        mListToday.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                updateFriend(mToday.getItem(position));
                return false;
            }
        });
        mListOther.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                updateFriend(mOther.getItem(position));
                return false;
            }
        });

        db = new FriendSqliteHelper(this);
    }

    private void updateFriend(Friend friend) {
        friend.setLastContacted(getToday());
        db.updateFriend(friend);
        Toast.makeText(MainActivity.this, "Updated "+friend.getName(), Toast.LENGTH_SHORT).show();
        refreshData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.new_friend:
                newFriend();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void newFriend() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.new_friend_dialog, null);
        final EditText name = (EditText) view.findViewById(R.id.name);
        final EditText days = (EditText) view.findViewById(R.id.days);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.add_friend, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        db.addFriend(new Friend(name.getText().toString(),
                                Integer.parseInt(days.getText().toString())));
                        Toast.makeText(MainActivity.this, "Added "+name.getText(), Toast.LENGTH_SHORT).show();
                        refreshData();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        builder.show();
    }

    public void refreshData() {
        mToday.clear();
        mOther.clear();

        mToday.addAll(getTodayFriends());
        mOther.addAll(getOtherFriends());
        mToday.notifyDataSetChanged();
        mOther.notifyDataSetChanged();
    }

    public List<Friend> getTodayFriends() {
        return db.getTodayFriends();
    }

    public List<Friend> getOtherFriends() {
        return db.getOtherFriends();
    }

    public Friend createFriend(String name, Integer days) {
        Friend friend = new Friend(name, days);
        db.addFriend(friend);
        return friend;
    }



    public static Integer getToday() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        DateTime now = new DateTime();
        return Days.daysBetween(epoch, now).getDays();
    }
}