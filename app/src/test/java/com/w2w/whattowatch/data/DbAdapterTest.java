package com.w2w.whattowatch.data;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Iñigo on 01/04/2016.
 */
public class DbAdapterTest extends AppCompatActivity{

    private DbAdapter adapter = new DbAdapter(this);

    //Con título nulo
    @Test
    public void testCreateSeries1() throws Exception {
        assertEquals(-1, adapter.createSeries(null, "Descripcion"));
    }

    //Con título <=0
    @Test
    public void testCreateSeries2() throws Exception {
        assertEquals(-1, adapter.createSeries("", "Descripcion"));
    }

    //Con descripción nula
    @Test
    public void testCreateSeries3() throws Exception {
        assertEquals(-1, adapter.createSeries("Titulo", null));
    }

    //Con título y descripción nulos
    @Test
    public void testCreateSeries4() throws Exception {
        assertEquals(-1, adapter.createSeries(null, null));
    }

    //Con títlo <=0 y descripción nula
    @Test
    public void testCreateSeries5() throws Exception {
        assertEquals(-1, adapter.createSeries("", null));
    }

    //Con título nulo y descripción < 0
    @Test
    public void testCreateSeries6() throws Exception {
        assertEquals(-1, adapter.createSeries(null, ""));
    }

    //Con títlo y descripción < 0
    @Test
    public void testCreateSeries7() throws Exception {
        assertEquals(-1, adapter.createSeries("", ""));
    }
    /*
    //Con descipción < 0
    @Test
    public void testCreateSeries8() throws Exception {
        assertTrue(adapter.createSeries("Titulo", "") >= 0);
    }

    //Create correcto
    @Test
    public void testCreateSeries9() throws Exception {
        assertTrue(adapter.createSeries("Titulo", "Descripcion") >= 0);
    }
    */

    //Con título nulo
    @Test
    public void testUpdateSeries1() throws Exception {
        assertEquals(false, adapter.updateSeries(null, "Descripcion", 1));
    }

    //Con título <=0
    @Test
    public void testUpdateSeries2() throws Exception {
        assertEquals(false, adapter.updateSeries("", "Descripcion", 1));
    }

    //Con descripción nula
    @Test
    public void testUpdateSeries3() throws Exception {
        assertEquals(false, adapter.updateSeries("Titulo", null, 1));
    }

    //Con título y descripción nulos
    @Test
    public void testUpdateSeries4() throws Exception {
        assertEquals(false, adapter.updateSeries(null, null, 1));
    }

    //Con títlo <=0 y descripción nula
    @Test
    public void testUpdateSeries5() throws Exception {
        assertEquals(false, adapter.updateSeries("", null, 1));
    }

    //Con título nulo y descripción < 0
    @Test
    public void testUpdateSeries6() throws Exception {
        assertEquals(false, adapter.updateSeries(null, "", 1));
    }

    //Con títlo y descripción < 0
    @Test
    public void testUpdateSeries7() throws Exception {
        assertEquals(false, adapter.updateSeries("", "", 1));
    }

    /*
    //Con descipción<=0
    @Test
    public void testUpdateSeries8() throws Exception {
        assertEquals(true, adapter.updateSeries("Titulo", "", 1));
    }

    //Update correcto
    @Test
    public void testCreateSeries9() throws Exception {
        assertEquals(true, adapter.updateSeries("Titulo", "Descripcion", 1));
    }
    */

    //Con título nulo e id<=0
    @Test
    public void testUpdateSeries10() throws Exception {
        assertEquals(false, adapter.updateSeries(null, "Descripcion", 0));
    }

    //Con título <=0 e id<=0
    @Test
    public void testUpdateSeries11() throws Exception {
        assertEquals(false, adapter.updateSeries("", "Descripcion", 0));
    }

    //Con descripción nula e id<=0
    @Test
    public void testUpdateSeries12() throws Exception {
        assertEquals(false, adapter.updateSeries("Titulo", null, 0));
    }

    //Con título y descripción nulos e id<=0
    @Test
    public void testUpdateSeries13() throws Exception {
        assertEquals(false, adapter.updateSeries(null, null, 0));
    }

    //Con títlo <=0 y descripción nula e id<=0
    @Test
    public void testUpdateSeries14() throws Exception {
        assertEquals(false, adapter.updateSeries("", null, 0));
    }

    //Con título nulo y descripción e id <= 0
    @Test
    public void testUpdateSeries15() throws Exception {
        assertEquals(false, adapter.updateSeries(null, "", 0));
    }

    //Con títlo, descripción e id <= 0
    @Test
    public void testUpdateSeries16() throws Exception {
        assertEquals(false, adapter.updateSeries("", "", 0));
    }

    //Con descripción e id<=0
    @Test
    public void testUpdateSeries17() throws Exception {
        assertEquals(false, adapter.updateSeries("Titulo", "", 0));
    }

    //Con id<=0
    @Test
    public void testUpdateSeries18() throws Exception {
        assertEquals(false, adapter.updateSeries("Titulo", "Descripcion", 0));
    }

    //Con id<=0
    @Test
    public void testDeleteSeries1() throws Exception {
        assertEquals(false, adapter.deleteSeries(0));
    }
    /*
    //Delete correcto
    @Test
    public void testDeleteSeries2() throws Exception {
        assertEquals(true, adapter.deleteSeries(1));
    }
    */

    //Con id<=0
    @Test
    public void testFetchSeries1() throws Exception {
        assertEquals(null, adapter.fetchSeries(0));
    }
    /*
    //Fetch correcto
    @Test
    public void testFetchSeries2() throws Exception {
        adapter.createSeries("Titulo", "Descripcion");
        Cursor c = adapter.fetchSeries(1);
        c.moveToFirst();
        assertEquals(c.getString(c.getColumnIndex("title")), "Titulo");
        assertEquals(c.getString(c.getColumnIndex("description")), "Descripcion");
    }
    */
    @Test
    public void testFetchAllSeries() throws Exception {

    }

    @Test
    public void testCreateEpisode() throws Exception {

    }

    @Test
    public void testUpdateEpisode() throws Exception {

    }

    @Test
    public void testDeleteEpisode() throws Exception {

    }

    @Test
    public void testFetchEpisode() throws Exception {

    }

    @Test
    public void testFetchAllEpisodes() throws Exception {

    }
}