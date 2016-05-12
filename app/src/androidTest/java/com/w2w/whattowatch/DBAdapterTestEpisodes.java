package com.w2w.whattowatch;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import com.w2w.whattowatch.activities.ListEpisodes;
import com.w2w.whattowatch.activities.ListSeries;
import com.w2w.whattowatch.data.DbAdapter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
//CUIDADO: La ejeución de esta clase borrará todas las entradas en la tabla "series"
@RunWith(AndroidJUnit4.class)
public class DBAdapterTestEpisodes extends ActivityInstrumentationTestCase2<ListSeries> {
    private ListSeries lSeries = null;
    private DbAdapter adapter = null;
    private long rowId;
    private long seriesID;
    private long firstSeriesID;

    public DBAdapterTestEpisodes() {
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
        SQLiteDatabase db = adapter.getsDb();
        db.execSQL("delete from series");
        db.execSQL("delete from sqlite_sequence where name = 'series'");
        firstSeriesID = adapter.createSeries("titulo", "descripcion", "5");
        seriesID = adapter.createSeries("titulo2", "descripcion2", "5");
    }

    @Override
    @After
    public void tearDown() throws Exception{
        if(rowId>0){
            adapter.deleteEpisode(rowId);
        }
        adapter.deleteSeries(seriesID);
        adapter.deleteSeries(firstSeriesID);
        super.tearDown();
    }

    //Test ok
    @Test
    public void createEpisodeTest1(){
        rowId = adapter.createEpisode("nombre", 5, 5, seriesID);
        assertTrue(rowId > 0);
    }

    //Test ok con valor límite en name
    @Test
    public void createEpisodeTest2()  {
        rowId = adapter.createEpisode("", 5, 5, seriesID);
        assertTrue(rowId > 0);
    }

    //Test ok con valor límite en season
    @Test
    public void createEpisodeTest3(){
        rowId = adapter.createEpisode("nombre", 1, 5, seriesID);
        assertTrue(rowId > 0);
    }

    //Test ok con valor límite en number
    @Test
    public void createEpisodeTest4(){
        rowId = adapter.createEpisode("nombre", 5, 1, seriesID);
        assertTrue(rowId > 0);
    }

    //Test ok con valor límite en series
    @Test
    public void createEpisodeTest5(){
        rowId = adapter.createEpisode("nombre", 5, 5, firstSeriesID);
        assertTrue(rowId > 0);
    }

    //Test no ok con name==null
    @Test
    public void createEpisodeTest6(){
        rowId = adapter.createEpisode(null, 5, 5, seriesID);
        assertTrue(rowId == -1);
    }

    /*
     * Test no ok con name==null, season<=0, number<=0 y
     * seriesID<=0
     */
    @Test
    public void createEpisodeTest7(){
        rowId = adapter.createEpisode(null, -1, -1, -1);
        assertTrue(rowId == -1);
    }

    //Test no ok con number<=0 y seriesID<=0
    @Test
    public void createEpisodeTest8(){
        rowId = adapter.createEpisode("nombre", 5, -1, -1);
        assertTrue(rowId == -1);
    }

    //Test no ok con season<=0 y seriesID<=0
    @Test
    public void createEpisodeTest9(){
        rowId = adapter.createEpisode("nombre", -1, 5, -1);
        assertTrue(rowId == -1);
    }

    //Test no ok con season<=0 y number<=0
    @Test
    public void createEpisodeTest10(){
        rowId = adapter.createEpisode("nombre", -1, -1, seriesID);
        assertTrue(rowId == -1);
    }

    //Test no ok con valor límite en season
    @Test
    public void createEpisodeTest11(){
        rowId = adapter.createEpisode("nombre", 0, 5, seriesID);
        assertTrue(rowId == -1);
    }

    //Test no ok con valor límite en number
    @Test
    public void createEpisodeTest12(){
        rowId = adapter.createEpisode("nombre", 5, 0, seriesID);
        assertTrue(rowId == -1);
    }

    //Test no ok con valor límite en number
    @Test
    public void createEpisodeTest13(){
        rowId = adapter.createEpisode("nombre", 5, 5, 0);
        assertTrue(rowId == -1);
    }
}