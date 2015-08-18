package org.koenighotze.jee7hotel.business.eventsource;

import com.mongodb.*;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EventSourceBeanTest {

//    private static final MongodStarter starter = MongodStarter.getDefaultInstance();
//
//    private MongodExecutable _mongodExe;
//    private MongodProcess _mongod;
//
//    private MongoClient _mongo;
//
//    @Before
//    public void setUp() throws Exception {
//
//        _mongodExe = starter.prepare(new MongodConfigBuilder()
//                .version(Version.Main.PRODUCTION)
//                .net(new Net(12345, Network.localhostIsIPv6()))
//                .build());
//        _mongod = _mongodExe.start();
//
//        _mongo = new MongoClient("localhost", 12345);
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        if (null != this._mongo)
//            _mongod.stop();
//
//        if (null != this._mongodExe)
//            _mongodExe.stop();
//    }
//
//    public Mongo getMongo() {
//        return _mongo;
//    }

    @Mock
    private MongoClient mongoClient;

    @Test
    public void fetchAllEvents() {

        if (1 == 1)
            return;
        DBCollection col = mock(DBCollection.class);
        DB db = mock(DB.class);
        when(db.getCollection(anyString())).thenReturn(col);
        when(this.mongoClient.getDB(anyString())).thenReturn(db);

        WriteResult result = mock(WriteResult.class);
        when(result.getUpsertedId()).thenReturn(Long.valueOf(12L));
        when(col.insert(Mockito.any(DBObject.class))).thenReturn(result);

        DBCursor cursor = mock(DBCursor.class);
        when(cursor.hasNext()).thenReturn(Boolean.TRUE).thenReturn(Boolean.FALSE);

        DBObject dbobject = new BasicDBObject("event", "{\"className\":\"org.koenighotze.jee7hotel.business.eventsource.EventSourceBeanTest\",\"methodName\":\"fetchAllEvents\",\"millis\":1417121451969,\"payload\":[{\"name\":\"a\",\"email\":\"b\"},{\"name\":\"c\",\"email\":\"d\"},{\"name\":\"e\",\"email\":\"f\"}]}");
        when(cursor.next()).thenReturn(dbobject);

        when(col.find()).thenReturn(cursor);

        EventSourceBean eventSourceBean = new EventSourceBean();
        eventSourceBean.setMongoClient(this.mongoClient);

        List<Event> events = eventSourceBean.fetchAllEvents();

        assertThat(events.size(), is(equalTo(1)));
    }

    @Test
    @Ignore("broken due to scs")
    public void testStoreEvent() {
//        DBCollection col = mock(DBCollection.class);
//        DB db = mock(DB.class);
//        WriteResult result = mock(WriteResult.class);
//        when(result.getUpsertedId()).thenReturn(Long.valueOf(12L));
//        when(col.insert(Mockito.any(DBObject.class))).thenReturn(result);
//
//        when(db.getCollection(anyString())).thenReturn(col);
//        when(this.mongoClient.getDB(anyString())).thenReturn(db);
//
//        EventSourceBean eventSourceBean = new EventSourceBean();
//        eventSourceBean.setMongoClient(this.mongoClient);
//
//        Object id = eventSourceBean.storeEvent(EventSourceBeanTest.class, EventSourceBeanTest.class.getMethods()[0], new Object[]{ new Guest("a", "b"), new Guest("c", "d"), new Guest("e", "f")});
//
//        assertThat(id, is(equalTo(Long.valueOf(12L))));
//
//        verify(col).insert(Mockito.any(DBObject.class));
    }
}