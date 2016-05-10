package com.w2w.whattowatch;

import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import com.w2w.whattowatch.activities.ListSeries;
import com.w2w.whattowatch.data.DbAdapter;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(AndroidJUnit4.class)
public class DBAdapterTestSeries extends ActivityInstrumentationTestCase2<ListSeries> {
    private ListSeries lSeries = null;
    private DbAdapter adapter = null;
    private long rowId;

    public DBAdapterTestSeries() {
        super(ListSeries.class);
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        lSeries = getActivity();
        adapter = lSeries.getmDbAdapter();
        rowId = 0;
    }

    @Override
    @After
    public void tearDown() throws Exception{
        if(rowId>0){
            assertTrue(adapter.deleteSeries(rowId));
        }
        super.tearDown();
    }

    //////////////////Pruebas de createSeries//////////////////////

    //Test ok con rating == 1, 2, 3, 4 ó 5
    @Test
    public void createSeriesTest1(){
        rowId = adapter.createSeries("titulo", "descripcion", "5");
        assertTrue(rowId > 0);
    }

    //Test ok con rating==null
    @Test
    public void createSeriesTest2(){
        rowId = adapter.createSeries("titulo", "descripcion", null);
        assertTrue(rowId > 0);
    }

    //Test ok con valor limite en titulo
    @Test
    public void createSeriesTest3(){
        rowId = adapter.createSeries("a", "descripcion", "5");
        assertTrue(rowId > 0);
    }

    //Test ok con valor limite en titulo
    @Test
    public void createSeriesTest4(){
        rowId = adapter.createSeries("titulo", "", "5");
        assertTrue(rowId > 0);
    }

    //Test no ok con titulo==null
    @Test
    public void createSeriesTest5(){
        rowId = adapter.createSeries(null, "descripcion", "5");
        assertTrue(rowId == -1);
    }

    //Test no ok con titulo vacío
    @Test
    public void createSeriesTest6(){
        rowId = adapter.createSeries("", "descripcion", "5");
        assertTrue(rowId == -1);
    }

    //Test no ok con descripción==null
    @Test
    public void createSeriesTest7(){
        rowId = adapter.createSeries("titulo", null, "5");
        assertTrue(rowId == -1);
    }

    //Test no ok con rating != 1, 2, 3, 4, 5 o null
    @Test
    public void createSeriesTest8(){
        rowId = adapter.createSeries("titulo", "descripcion", "6");
        assertTrue(rowId == -1);
    }

    //Test no ok con título vacío y descripción==null
    @Test
    public void createSeriesTest9(){
        rowId = adapter.createSeries("", null, "5");
        assertTrue(rowId == -1);
    }

    /*
     * Test no ok con título vacío, descripción==null
     * y rating != 1, 2, 3, 4, 5 o null
     */
    @Test
    public void createSeriesTest10(){
        rowId = adapter.createSeries("", null, "6");
        assertTrue(rowId == -1);
    }

    /*
     * Test no ok con título==null, descripción==null
     * y rating != 1, 2, 3, 4, 5 o null
     */
    @Test
    public void createSeriesTest11(){
        rowId = adapter.createSeries(null, null, "6");
        assertTrue(rowId == -1);
    }

    //////////////////Pruebas de updateSeries//////////////////////

    //Test ok con rating == 1, 2, 3, 4, 5
    @Test
    public void updateSeriesTest1(){
        rowId = adapter.createSeries("titulo", "descripcion", "5");
        assertTrue(adapter.updateSeries("titulo", "descripcion", "5", rowId));
    }

    //Test ok con rating == null
    @Test
    public void updateSeriesTest2(){
        rowId = adapter.createSeries("titulo", "descripcion", "5");
        assertTrue(adapter.updateSeries("titulo", "descripcion", null, rowId));
    }

    //Test ok con valor límite en título
    @Test
    public void updateSeriesTest3(){
        rowId = adapter.createSeries("titulo", "descripcion", "5");
        assertTrue(adapter.updateSeries("a", "descripcion", "5", rowId));
    }

    //Test ok con valor límite en descripción
    @Test
    public void updateSeriesTest4(){
        rowId = adapter.createSeries("titulo", "descripcion", "5");
        assertTrue(adapter.updateSeries("titulo", "", "5", rowId));
    }

    //Test ok con valor límite en rowID
    @Test
    public void updateSeriesTest5(){
        SQLiteDatabase db = adapter.getsDb();
        db.execSQL("delete from notes");
        db.execSQL("delete from sqlite_sequence where name = 'notes'");
        rowId = adapter.createSeries("titulo", "descripcion", "5");
        assertTrue(rowId==1);
        assertTrue(adapter.updateSeries("a", "descripcion", "5", rowId));
    }

    //Test no ok con título vacío
    @Test
    public void updateSeriesTest6(){
        rowId = adapter.createSeries("titulo", "descripcion", "5");
        assertFalse(adapter.updateSeries("", "descripcion", "5", rowId));
    }

    /*
     * Test no ok con título==null, descripción==null,
     * rating != 1, 2, 3, 4, 5 o null y seriesID<=0
     */
    @Test
    public void updateSeriesTest7(){
        rowId = adapter.createSeries("titulo", "descripcion", "5");
        assertFalse(adapter.updateSeries(null, null, "6", -1));
    }

    //Test no ok con título==null y descripción==null
    @Test
    public void updateSeriesTest8(){
        rowId = adapter.createSeries("titulo", "descripcion", "5");
        assertFalse(adapter.updateSeries(null, null, "5", rowId));
    }

    /*
     * Test no ok con título vacío, rating != 1, 2, 3, 4, 5 o null
     * y seriesID<=0
     */
    @Test
    public void updateSeriesTest9(){
        rowId = adapter.createSeries("titulo", "descripcion", "5");
        assertFalse(adapter.updateSeries("", "descripcion", "6", -1));
    }

    //Test no ok con seriesID<=0
    @Test
    public void updateSeriesTest10(){
        rowId = adapter.createSeries("titulo", "descripcion", "5");
        assertFalse(adapter.updateSeries("titulo", "descripcion", "5", -1));
    }

    //Test no ok con valor límite en seriesID
    @Test
    public void updateSeriesTest11(){
        rowId = adapter.createSeries("titulo", "descripcion", "5");
        assertFalse(adapter.updateSeries("titulo", "descripcion", "5", 0));
    }

    //Test no ok con descripción==null y rating != 1, 2, 3, 4, 5 o null
    @Test
    public void updateSeriesTest12(){
        rowId = adapter.createSeries("titulo", "descripcion", "5");
        assertFalse(adapter.updateSeries("titulo", null, "6", rowId));
    }

    //Test no ok con título vacío y descripción==null
    @Test
    public void updateSeriesTest13(){
        rowId = adapter.createSeries("titulo", "descripcion", "5");
        assertFalse(adapter.updateSeries("", null, "5", rowId));
    }

    //Test no ok con título==null, descripción==null y seriesID<=0
    @Test
    public void updateSeriesTest14(){
        rowId = adapter.createSeries("titulo", "descripcion", "5");
        assertFalse(adapter.updateSeries(null, null, "5", -1));
    }

    //Test no ok con título==null
    @Test
    public void updateSeriesTest15(){
        rowId = adapter.createSeries("titulo", "descripcion", "5");
        assertFalse(adapter.updateSeries(null, "descripcion", "5", rowId));
    }
}