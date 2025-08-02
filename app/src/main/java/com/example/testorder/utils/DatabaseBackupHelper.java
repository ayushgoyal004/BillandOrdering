package com.example.testorder.utils;

import android.content.Context;
import android.os.Environment;
import java.io.*;

public class DatabaseBackupHelper {

    public static void backupDatabase(Context context) {
        try {
            File dbFile = context.getDatabasePath("order_billing_database");

            File backupDir = new File(context.getExternalFilesDir(null), "backup");
            if (!backupDir.exists()) backupDir.mkdirs();

            File backupFile = new File(backupDir, "order_billing_backup.db");

            try (InputStream in = new FileInputStream(dbFile);
                 OutputStream out = new FileOutputStream(backupFile)) {

                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
            }

            System.out.println("Backup saved at: " + backupFile.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}