package net.kazed.ambient.fragment;

import java.io.File;

import net.kazed.ambient.R;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FileChooserActivity extends ListActivity {

    private static final int FILE_CHOSEN = 1;

    private ArrayAdapter<String> adapter;
    private File directory;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_chooser);
        
        Uri directoryUri = getIntent().getData();
        if (directoryUri == null) {
            directory = Environment.getExternalStorageDirectory();
        } else {
            directory = new File(directoryUri.getEncodedPath());
        }
        
//        List<String> documents = new ArrayList<String>();
//        for (int i = 0; i < 10; i++) {
//            documents.add("File" + i);
//        }
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.file_list_item_view, R.id.file_name);
        setListAdapter(adapter);
        
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String fileName = adapter.getItem(position);
        File file = new File(directory, fileName);
        if (file.isFile()) {
            Uri.Builder uriBuilder = new Uri.Builder();
            uriBuilder.scheme("file").appendEncodedPath(file.getAbsolutePath());
            Intent intent = new Intent(getIntent().getAction(), uriBuilder.build());
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Uri.Builder uriBuilder = new Uri.Builder();
            uriBuilder.scheme("file").appendEncodedPath(file.getAbsolutePath());
            Intent intent = new Intent(this, FileChooserActivity.class);
            intent.setData(uriBuilder.build());
            startActivityForResult(intent, FILE_CHOSEN);
        }
    }

    @Override
    protected void onResume() {
        adapter.clear();
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                if (file.getName().endsWith(".mp3")) {
                    adapter.add(file.getName());
                }
            } else {
            	adapter.add(file.getName());
            }
        }
        super.onResume();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
        case FILE_CHOSEN:
        	if (data != null) {
                Intent intent = new Intent(data.getAction(), data.getData());
                setResult(RESULT_OK, intent);
                finish();
        	}
            break;
        }
    }
    

}