package com.mycompany.app;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Polygon;
import com.mongodb.client.model.geojson.Position;
import org.bson.Document;
import org.bson.conversions.Bson;

// begin importsFindExample
import java.util.Arrays;

import static com.mongodb.client.model.Filters.near;
import static com.mongodb.client.model.Filters.geoWithin;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Projections.excludeId;
//end importsFindExample'

public class Geo {

    private final MongoClient mongoClient;

    public Geo() {
        final String uri = System.getenv("DRIVER_REF_URI");
        mongoClient = MongoClients.create(uri);

    }

    public void go() {
        Geo geo = new Geo();
        System.out.println("Near Example: ");
        geo.nearExample();
        System.out.println("Range Example: ");
        geo.rangeExample();

    }

    private void nearExample() {
        // begin findExample

        // code to set up your mongo client ...
        MongoDatabase database = mongoClient.getDatabase("sample_mflix");
        MongoCollection<Document> collection = database.getCollection("theaters");
        final Point centralPark = new Point(new Position(-73.9667, 40.78));
        final Bson query = near("location.geo", centralPark, 10000.0, 5000.0);
        final Bson projection = fields(include("location.address.city"), excludeId());
        collection.find(query)
                .projection(projection)
                .forEach(doc -> System.out.println(doc.toJson()));
        // end findExample
    }

    private void rangeExample() {
        MongoDatabase database = mongoClient.getDatabase("sample_mflix");
        MongoCollection<Document> collection = database.getCollection("theaters");
        // begin rangeExample

        // code to set up your mongo collection ...
        Polygon longIslandTriangle = new Polygon(Arrays.asList(new Position(-72, 40),
                new Position(-74, 41),
                new Position(-72, 39),
                new Position(-72, 40)));
        final Bson projection = fields(include("location.address.city"), excludeId());
        Bson geoWithinComparison = geoWithin("location.geo", longIslandTriangle);
        collection.find(geoWithinComparison)
                .projection(projection)
                .forEach(doc -> System.out.println(doc.toJson()));
        // end rangeExample
    }
}