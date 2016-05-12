package com.w2w.whattowatch;

import android.database.Cursor;
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

    //////////////////Pruebas de createEpisode//////////////////////

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

    //////////////////Pruebas de updateEpisode//////////////////////

    //Test ok
    @Test
    public void updateEpisodeTest1(){
        rowId = adapter.createEpisode("nombre", 5, 5, seriesID);
        assertTrue(adapter.updateEpisode("nombre", 5, 5, rowId));
    }

    //Test ok con valor límite en name
    @Test
    public void updateEpisodeTest2(){
        rowId = adapter.createEpisode("nombre", 5, 5, seriesID);
        assertTrue(adapter.updateEpisode("", 5, 5, rowId));
    }

    //Test ok con valor límite en season
    @Test
    public void updateEpisodeTest3(){
        rowId = adapter.createEpisode("nombre", 5, 5, seriesID);
        assertTrue(adapter.updateEpisode("nombre", 1, 5, rowId));
    }

    //Test ok con valor límite en number
    @Test
    public void updateEpisodeTest4(){
        rowId = adapter.createEpisode("nombre", 5, 5, seriesID);
        assertTrue(adapter.updateEpisode("nombre", 5, 1, rowId));
    }

    //Test ok con valor límite en episodeID
    //CUIDADO: Este test borra todas las entradas de la tabla "episodes"
    @Test
    public void updateEpisodeTest5(){
        SQLiteDatabase db = adapter.getsDb();
        db.execSQL("delete from episodes");
        db.execSQL("delete from sqlite_sequence where name = 'episodes'");
        rowId = adapter.createEpisode("nombre", 5, 5, seriesID);
        assertTrue(rowId==1);
        assertTrue(adapter.updateEpisode("nombre", 5, 5, rowId));
    }

    //Test no ok con name==null
    @Test
    public void updateEpisodeTest6(){
        rowId = adapter.createEpisode("nombre", 5, 5, seriesID);
        assertFalse(adapter.updateEpisode(null, 5, 5, rowId));
    }

    /*
     * Test no ok con name==null, season<=0, number<=0
     * y episodeID<=0
     */
    @Test
    public void updateEpisodeTest7(){
        assertFalse(adapter.updateEpisode(null, -1, -1, -1));
    }

    //Test no ok con number<=0 y episodeID<=0
    @Test
    public void updateEpisodeTest8(){
        assertFalse(adapter.updateEpisode("nombre", 5, -1, -1));
    }

    //Test no ok con season<=0 y episodeID<=0
    @Test
    public void updateEpisodeTest9(){
        assertFalse(adapter.updateEpisode("nombre", -1, 5, -1));
    }

    //Test no ok con season<=0 y number<=0
    @Test
    public void updateEpisodeTest10(){
        rowId = adapter.createEpisode("nombre", 5, 5, seriesID);
        assertFalse(adapter.updateEpisode("nombre", -1, -1, rowId));
    }

    //Test no ok con valor límite en season
    @Test
    public void updateEpisodeTest11(){
        rowId = adapter.createEpisode("nombre", 5, 5, seriesID);
        assertFalse(adapter.updateEpisode("nombre", 0, 5, rowId));
    }

    //Test no ok con valor límite en number
    @Test
    public void updateEpisodeTest12(){
        rowId = adapter.createEpisode("nombre", 5, 5, seriesID);
        assertFalse(adapter.updateEpisode("nombre", 5, 0, rowId));
    }

    //Test no ok con valor límite en episodeID
    @Test
    public void updateEpisodeTest13(){
        assertFalse(adapter.updateEpisode("nombre", 5, 5, 0));
    }

    //////////////////Pruebas de toggleWatched//////////////////////

    //Test ok
    @Test
    public void toggleWatchedTest1(){
        rowId = adapter.createEpisode("nombre", 5, 5, seriesID);
        //Compruebo si se marca como visto (tras crearlo es null)
        adapter.toggleWatched(rowId);
        SQLiteDatabase db = adapter.getsDb();
        String query = "SELECT watched FROM episodes WHERE _id = " + rowId;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        int watchedIndex = c.getColumnIndex("watched");
        String watched = c.getString(watchedIndex);
        assertEquals("1", watched);
        //Compruebo si se desmarca como visto
        adapter.toggleWatched(rowId);
        c = db.rawQuery(query, null);
        c.moveToFirst();
        watchedIndex = c.getColumnIndex("watched");
        watched = c.getString(watchedIndex);
        assertEquals("0", watched);
    }

    //Test ok con valor límite en episodeID
    //CUIDADO: Este test borra todas las entradas de la tabla "episodes"
    @Test
    public void toggleWatchedTest2(){
        SQLiteDatabase db = adapter.getsDb();
        db.execSQL("delete from episodes");
        db.execSQL("delete from sqlite_sequence where name = 'episodes'");
        rowId = adapter.createEpisode("nombre", 5, 5, seriesID);
        assertTrue(rowId==1);
        //Compruebo si se marca como visto (tras crearlo es null)
        adapter.toggleWatched(rowId);
        String query = "SELECT watched FROM episodes WHERE _id = " + rowId;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        int watchedIndex = c.getColumnIndex("watched");
        String watched = c.getString(watchedIndex);
        assertEquals("1", watched);
        //Compruebo si se desmarca como visto
        adapter.toggleWatched(rowId);
        c = db.rawQuery(query, null);
        c.moveToFirst();
        watchedIndex = c.getColumnIndex("watched");
        watched = c.getString(watchedIndex);
        assertEquals("0", watched);
    }

    //Test no ok con episodeID<=0 -> Resultado: no pasa nada.
    @Test
    public void toggleWatchedTest3(){
        adapter.toggleWatched(-1);
    }

    //Test no ok con valor límite en episodeID -> Resultado: no pasa nada.
    @Test
    public void toggleWatchedTest4(){
        adapter.toggleWatched(0);
    }

    //////////////////Pruebas de deleteEpisode//////////////////////

    //Test ok
    @Test
    public void deleteEpisodeTest1(){
        rowId = adapter.createEpisode("nombre", 5, 5, seriesID);
        assertTrue(adapter.deleteEpisode(rowId));
    }

    //Test ok con valor límite en episodeID
    //CUIDADO: Este test borra todas las entradas de la tabla "episodes"
    @Test
    public void deleteEpisodeTest2(){
        SQLiteDatabase db = adapter.getsDb();
        db.execSQL("delete from episodes");
        db.execSQL("delete from sqlite_sequence where name = 'episodes'");
        rowId = adapter.createEpisode("nombre", 5, 5, seriesID);
        assertTrue(rowId==1);
        assertTrue(adapter.deleteEpisode(rowId));
    }

    //Test no ok con episodeID<=0
    @Test
    public void deleteEpisodeTest3(){
        assertFalse(adapter.deleteEpisode(-1));
    }

    //Test no ok con valor límite en episodeID
    @Test
    public void deleteEpisodeTest4(){
        assertFalse(adapter.deleteEpisode(0));
    }

    //////////////////Pruebas de fetchEpisode//////////////////////

    //Test ok
    @Test
    public void fetchEpisodeTest1(){
        rowId = adapter.createEpisode("nombre", 5, 5, seriesID);
        Cursor c = adapter.fetchEpisode(rowId);
        assertTrue(c.getCount()==1);
        c.moveToFirst();
        int idIndex = c.getColumnIndex("_id");
        assertEquals(rowId, c.getInt(idIndex));
    }

    //Test ok con valor límite en episodeID
    @Test
    public void fetchEpisodeTest2(){
        SQLiteDatabase db = adapter.getsDb();
        db.execSQL("delete from episodes");
        db.execSQL("delete from sqlite_sequence where name = 'episodes'");
        rowId = adapter.createEpisode("nombre", 5, 5, seriesID);
        assertTrue(rowId==1);
        Cursor c = adapter.fetchEpisode(rowId);
        assertTrue(c.getCount()==1);
        c.moveToFirst();
        int idIndex = c.getColumnIndex("_id");
        assertEquals(rowId, c.getInt(idIndex));
    }

    //Test no ok con episodeID<=0
    @Test
    public void fetchEpisodeTest3(){
        Cursor c = adapter.fetchEpisode(-1);
        assertTrue(c.getCount()==0);
    }

    //Test no ok con valor límite en episodeID
    @Test
    public void fetchEpisodeTest4(){
        Cursor c = adapter.fetchEpisode(0);
        assertTrue(c.getCount()==0);
    }

    //////////////////Pruebas de getSeasons//////////////////////

    //Test ok con seriesID con 0 y con 1 temporadas.
    @Test
    public void getSeasonsTest1(){
        Cursor c = adapter.getSeasons(seriesID);
        assertTrue(c.getCount()==0);
        rowId = adapter.createEpisode("nombre", 5, 5, seriesID);
        c = adapter.getSeasons(seriesID);
        assertTrue(c.getCount()>0);
    }

    //Test ok con valor límite en seriesID y con 0 y con 1 temporadas.
    @Test
    public void getSeasonsTest2(){
        Cursor c = adapter.getSeasons(firstSeriesID);
        assertTrue(c.getCount()==0);
        rowId = adapter.createEpisode("nombre", 5, 5, firstSeriesID);
        c = adapter.getSeasons(firstSeriesID);
        assertTrue(c.getCount() > 0);
    }
    //Test no ok con seriesID<=0
    @Test
    public void getSeasonsTest3(){
        Cursor c = adapter.getSeasons(-1);
        assertTrue(c.getCount()==0);
    }

    //Test no ok con valor límite en seriesID
    @Test
    public void getSeasonsTest4(){
        Cursor c = adapter.getSeasons(0);
        assertTrue(c.getCount()==0);
    }

    //////////////////Pruebas de fetchEpisodesFromSeason//////////////////////

    //Test ok
    @Test
    public void fetchEpisodesFromSeasonTest1(){
        rowId = adapter.createEpisode("nombre", 5, 5, seriesID);
        Cursor c = adapter.fetchEpisodesFromSeason(seriesID, 5);
        assertTrue(c.getCount()>0);
    }

    //Test ok con valor límite en seriesID
    @Test
    public void fetchEpisodesFromSeasonTest2(){
        rowId = adapter.createEpisode("nombre", 5, 5, firstSeriesID);
        Cursor c = adapter.fetchEpisodesFromSeason(firstSeriesID, 5);
        assertTrue(c.getCount()>0);
    }

    //Test ok con valor límite en season
    @Test
    public void fetchEpisodesFromSeasonTest3(){
        rowId = adapter.createEpisode("nombre", 1, 5, seriesID);
        Cursor c = adapter.fetchEpisodesFromSeason(seriesID, 1);
        assertTrue(c.getCount()>0);
    }

    //Test no ok con season<=0
    @Test
    public void fetchEpisodesFromSeasonTest4(){
        Cursor c = adapter.fetchEpisodesFromSeason(seriesID, -1);
        assertTrue(c.getCount()==0);
    }

    //Test no ok con season<=0
    @Test
    public void fetchEpisodesFromSeasonTest5(){
        Cursor c = adapter.fetchEpisodesFromSeason(-1, 5);
        assertTrue(c.getCount()==0);
    }

    //Test no ok con season<=0 y seriesID<=0
    @Test
    public void fetchEpisodesFromSeasonTest6(){
        Cursor c = adapter.fetchEpisodesFromSeason(-1, -1);
        assertTrue(c.getCount()==0);
    }

    //Test no ok con valor límite en season
    @Test
    public void fetchEpisodesFromSeasonTest7(){
        Cursor c = adapter.fetchEpisodesFromSeason(seriesID, 0);
        assertTrue(c.getCount()==0);
    }

    //Test no ok con valor límite en season
    @Test
    public void fetchEpisodesFromSeasonTest8(){
        Cursor c = adapter.fetchEpisodesFromSeason(0, 5);
        assertTrue(c.getCount()==0);
    }
}