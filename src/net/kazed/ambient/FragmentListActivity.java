package net.kazed.ambient;

import net.kazed.ambient.database.AudioFragmentTable;
import net.kazed.ambient.database.ManagedAudioFragmentDao;
import net.kazed.android.inject.InjectedListActivity;
import net.kazed.android.inject.Resource;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class FragmentListActivity extends InjectedListActivity {

    private static final int MENU_ADD_FRAGMENT = Menu.FIRST;
    private static final int NEW_FRAGMENT = 1;
    
    private ManagedAudioFragmentDao audioFragmentDao;
    private ManagedAudioFragmentDao.RecordQuery query;
    private SimpleCursorAdapter adapter;
    
    /**
     * @param audioFragmentDao DAO for AudioFragment.
     */
    @Resource("audioFragmentDao")
    public void setAudioFragmentDao(ManagedAudioFragmentDao audioFragmentDao) {
    	this.audioFragmentDao = audioFragmentDao;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
//        AmbientPlayerApplication application = (AmbientPlayerApplication) getApplication();
//        application.injectInto(this);
        
        setContentView(R.layout.player);
        
        query = audioFragmentDao.queryFragment(this, AudioFragment.CONTENT_URI);
        adapter = new SimpleCursorAdapter(this, R.layout.fragment_list_item_view, query.getCursor(),
                        new String[] { AudioFragmentTable.COLUMN_NAME },
                        new int[] { R.id.name });
        setListAdapter(adapter);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        AudioFragment fragment = query.getRecord(position);
        Uri itemUri = AudioFragment.CONTENT_URI.buildUpon().appendPath(Long.toString(fragment.getId())).build();
        Intent fragmentIntent = new Intent(Intent.ACTION_VIEW, itemUri);
        startActivity(fragmentIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        String menuName = getResources().getString(R.string.menu_add_fragment);
        MenuItem item = menu.add(0, MENU_ADD_FRAGMENT, 0, menuName).setIcon(android.R.drawable.ic_menu_add);
        item.setAlphabeticShortcut('a');

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case MENU_ADD_FRAGMENT:
            Intent fragmentIntent = new Intent(Intent.ACTION_INSERT, AudioFragment.CONTENT_URI);
            startActivityForResult(fragmentIntent, NEW_FRAGMENT);
            break;
        }
        return super.onOptionsItemSelected(item);
    }


}