package com.example.MyToDo;

import android.app.*;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;

public class MyActivity extends Activity {
    private ArrayList<Doing> doings;
    private DoingsAdapter doingsAdapter;
    static String newDoing;
    static String newDate;
    private DatePicker datePicker;
    AdapterView.AdapterContextMenuInfo info;
    private static final int NEW_DOING = -1;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        newDate = (String) DateFormat.format("dd.MM.yyyy", System.currentTimeMillis());
        createDoings();
        fillList();
    }

    private void fillList() {
        ListView lvDoings = (ListView) findViewById(R.id.lvDoings);
        registerForContextMenu(lvDoings);
        doingsAdapter = new DoingsAdapter(doings, this);
        lvDoings.setAdapter(doingsAdapter);
    }

    private void createDoings() {
        doings = new ArrayList<Doing>();
        if (readMyObject() != null) doings = readMyObject();
        Button createDoingButton = (Button) findViewById(R.id.createDoingButton);
        createDoingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newDoing = "";
                createAlertDialogEditDoing(NEW_DOING);
            }
        });
    }

    private void createAlertDialogEditDoing(final int position) {
        final View newDoingInput = getLayoutInflater().inflate(R.layout.newdoing_input, null);
        final EditText etNewDoing = (EditText) newDoingInput.findViewById(R.id.input_todo);
        final TextView etNewDate = (TextView) newDoingInput.findViewById(R.id.input_date);
        etNewDoing.setText(newDoing);
        etNewDate.setText(newDate);
        new AlertDialog.Builder(MyActivity.this)
                .setView(newDoingInput)
                .setNeutralButton("Выбрать дату", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newDoing = etNewDoing.getText().toString();
                        createAlertDialogNewDate(position);
//                        showDatePickerDialog(newDoingInput);
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newDoing = etNewDoing.getText().toString();
                        newDate = etNewDate.getText().toString();
                        Doing doing = new Doing();
                        doing.setToDo(newDoing);
                        doing.setDate(newDate);
                        if (position == NEW_DOING) doings.add(doing);
                        else {
                            doings.get(position).setToDo(newDoing);
                            doings.get(position).setDate(newDate);
                        }
                        sort();
                        writeMyObject(doings);
                        fillList();
                    }
                })
                .create()
                .show();
    }

    private void sort() {
        Doing doing = new Doing();
        for (int i = 0; i < doings.size() - 1; i++) {
            for (int j = i + 1; j < doings.size(); j++) {
                if (toInt(doings.get(i).getDate()) > toInt(doings.get(j).getDate())) {
                    doing = doings.get(i);
                    doings.set(i, doings.get(j));
                    doings.set(j, doing);
                }
            }
        }
    }

    int toInt(String s) {
        String ss[] = s.split("");
        int result = 0;
        result = Integer.parseInt(ss[1]) * 10 + Integer.parseInt(ss[2]) +
                Integer.parseInt(ss[4]) * 1000 + Integer.parseInt(ss[5]) * 100;
        return result;
    }

    private void createAlertDialogNewDate(final int position) {
        final View newDateInput = getLayoutInflater().inflate(R.layout.newdate_input, null);
        datePicker = (DatePicker) newDateInput.findViewById(R.id.datePicker);
        new AlertDialog.Builder(MyActivity.this)
                .setView(newDateInput)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String month = datePicker.getMonth() + 1 > 9 ? String.valueOf(datePicker.getMonth() + 1) : "0" +
                                String.valueOf(datePicker.getMonth() + 1);
                        String day = datePicker.getDayOfMonth() > 9 ? String.valueOf(datePicker.getDayOfMonth()) : "0" +
                                String.valueOf(datePicker.getDayOfMonth());
                        newDate = day + "." + month + "." + String.valueOf(datePicker.getYear());
                        createAlertDialogEditDoing(position);
                    }
                })
                .create()
                .show();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.contextmenu, menu);
        info = (AdapterView.AdapterContextMenuInfo) menuInfo;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_button:
                newDate = doings.get(info.position).getDate();
                newDoing = doings.get(info.position).getToDo();
                createAlertDialogEditDoing(info.position);
                break;
            case R.id.delete_button:
                doings.remove(info.position);
                writeMyObject(doings);
                doingsAdapter.notifyDataSetChanged();
                break;
            case R.id.done_button:
                doings.get(info.position).setIsDone(true);
                writeMyObject(doings);
                doingsAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
        return true;
    }

    private ArrayList<Doing> readMyObject() {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = openFileInput("content.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            ArrayList<Doing> doings = (ArrayList<Doing>) objectInputStream.readObject();
            objectInputStream.close();
            return doings;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void writeMyObject(ArrayList<Doing> doings) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput("content.txt", MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(doings);
            objectOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showDatePickerDialog(View view) {
        DialogFragment dialogFragment = new DatePickerFragment();
        dialogFragment.show(getFragmentManager(), "datePicker");
//        createAlertDialogEditDoing();
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener, DialogInterface.OnClickListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            newDate = i2 + "." + (i1 + 1) + "." + i;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    }
}
