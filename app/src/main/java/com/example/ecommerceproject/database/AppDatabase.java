package com.example.ecommerceproject.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.ecommerceproject.dao.AccountDao;
import com.example.ecommerceproject.dao.OrderDao;
import com.example.ecommerceproject.dao.OrderItemDao;
import com.example.ecommerceproject.dao.ProductDao;
import com.example.ecommerceproject.entities.Account;
import com.example.ecommerceproject.entities.Order;
import com.example.ecommerceproject.entities.OrderItem;
import com.example.ecommerceproject.entities.Product;

@Database(entities = {Account.class, Product.class, OrderItem.class, Order.class}, version = 11)
public abstract class AppDatabase extends RoomDatabase {
    public static AppDatabase appDatabase;

    public static synchronized AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context,
                    AppDatabase.class, "app").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }

        // returns the singleton object
        return appDatabase;

    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(appDatabase).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private ProductDao productDao;

        private PopulateDbAsyncTask(AppDatabase appDatabase) {
            productDao = appDatabase.productDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (productDao.getAll().size() > 0) {
                return null;
            }
            productDao.insertAll(
                    new Product()
                            .setName("Apple Watch")
                            .setDescription("Available when you purchase any new iPhone, iPad, iPod Touch, Mac or Apple TV, £4.99/month after free trial.")
                            .setPrice(1000.0)
                            .setQuantity(10L)
                            .setImageUrl("https://i0.wp.com/danzis.org/wp-content/uploads/2020/04/black-apple-watch.png?ssl=1"),
                    new Product()
                            .setName("IPhone 14")
                            .setDescription("iPhone 14 Pro. Capture impressive details with the 48MP Main Camera. Experience iPhone in a whole new way with Dynamic Island and the Always On display. Collision Detection, a new safety feature, calls for help when needed.")
                            .setPrice(2000.0)
                            .setQuantity(20L)
                            .setImageUrl("https://media.croma.com/image/upload/v1662703724/Croma%20Assets/Communication/Mobiles/Images/261934_qgssvy.png"),
                    new Product()
                            .setName("Macbook M2")
                            .setDescription("M2 is the next generation of Apple silicon. Its 8-core CPU lets you zip through everyday tasks like creating documents and presentations, or take on more intensive workflows like developing in Xcode or mixing tracks in Logic Pro.")
                            .setPrice(1000.0)
                            .setQuantity(10L)
                            .setImageUrl("https://vuatao.vn/wp-content/uploads/2021/10/macbook-pro-16-m1-pro-2021-xam-650x650-2.png"),
                    new Product()
                            .setName("Air Pod 2")
                            .setDescription("AirPods 2 Lightning Charge Apple MV7N2 Bluetooth headset - dubbed a national legendary AirPods that is very popular with apple fans.")
                            .setPrice(3000.0)
                            .setQuantity(10L)
                            .setImageUrl("https://assets.stickpng.com/images/60b79e8771a1fd000411f6be.png"),
                    new Product()
                            .setName("Air Tag")
                            .setDescription("Airtag is a small device with integrated Bluetooth technology used to find lost objects and equipment. Although there are many similar products, Apple's smart home accessories promise to integrate technology more deeply, allowing users to experience even more wonderful activities of the device.")
                            .setPrice(1000.0)
                            .setQuantity(10L)
                            .setImageUrl("https://www.uwalumnistore.com/storeimages/294-1693423-3_hi.png")
            );
            return null;
        }
    }

    public abstract AccountDao accountDao();

    public abstract ProductDao productDao();

    public abstract OrderDao orderDao();

    public abstract OrderItemDao orderItemDao();
}