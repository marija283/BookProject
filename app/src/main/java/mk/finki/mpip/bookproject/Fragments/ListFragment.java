package mk.finki.mpip.bookproject.Fragments;

import android.app.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import mk.finki.mpip.bookproject.BookDetailActivity;
import mk.finki.mpip.bookproject.Entities.Book;
import mk.finki.mpip.bookproject.HomeActivity;
import mk.finki.mpip.bookproject.R;
import mk.finki.mpip.bookproject.Tasks.GetAllBooksAdapter;
import mk.finki.mpip.bookproject.Tasks.GetAllBooksTask;

/**
 * Created by Riste on 15.7.2016.
 */
public class ListFragment extends Fragment {

    private ListView listView;
    //private ArrayAdapter<Book> adapter;
    private List<Book> bookList;
    private GetAllBooksAdapter customAdapter;
    private GetAllBooksTask bookTask;

    //create the Fragment
    public static ListFragment create() {
        ListFragment fragment = new ListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        doInject(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        callAsyincTask();
    }

    private void doInject(View view){
        bookList = new ArrayList<Book>();
       // adapter = new ArrayAdapter<Book>(view.getContext(),android.R.layout.simple_list_item_1,bookList);
        customAdapter = new GetAllBooksAdapter(getActivity(),bookList);

        listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(customAdapter);

        bookTask = new GetAllBooksTask(getActivity(),customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = bookList.get(position);

                Intent i = new Intent( getActivity(), BookDetailActivity.class);
                getActivity().startActivity(i);
            }
        });
    }

    public void callAsyincTask(){
        if (bookTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
            bookTask = new GetAllBooksTask(getActivity(),customAdapter);
        }
        if (bookTask.getStatus().equals(AsyncTask.Status.PENDING))
            bookTask.execute();
    }

}
