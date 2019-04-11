package com.spd.qsevendemo.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SEVEN_BEAN".
*/
public class SevenBeanDao extends AbstractDao<SevenBean, Long> {

    public static final String TABLENAME = "SEVEN_BEAN";

    /**
     * Properties of entity SevenBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Name = new Property(0, String.class, "name", false, "NAME");
        public final static Property Check = new Property(1, boolean.class, "check", false, "CHECK");
        public final static Property Code = new Property(2, int.class, "code", false, "CODE");
        public final static Property Id = new Property(3, Long.class, "id", true, "_id");
    }


    public SevenBeanDao(DaoConfig config) {
        super(config);
    }
    
    public SevenBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SEVEN_BEAN\" (" + //
                "\"NAME\" TEXT," + // 0: name
                "\"CHECK\" INTEGER NOT NULL ," + // 1: check
                "\"CODE\" INTEGER NOT NULL ," + // 2: code
                "\"_id\" INTEGER PRIMARY KEY );"); // 3: id
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SEVEN_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, SevenBean entity) {
        stmt.clearBindings();
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(1, name);
        }
        stmt.bindLong(2, entity.getCheck() ? 1L: 0L);
        stmt.bindLong(3, entity.getCode());
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(4, id);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, SevenBean entity) {
        stmt.clearBindings();
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(1, name);
        }
        stmt.bindLong(2, entity.getCheck() ? 1L: 0L);
        stmt.bindLong(3, entity.getCode());
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(4, id);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3);
    }    

    @Override
    public SevenBean readEntity(Cursor cursor, int offset) {
        SevenBean entity = new SevenBean( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // name
            cursor.getShort(offset + 1) != 0, // check
            cursor.getInt(offset + 2), // code
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3) // id
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, SevenBean entity, int offset) {
        entity.setName(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setCheck(cursor.getShort(offset + 1) != 0);
        entity.setCode(cursor.getInt(offset + 2));
        entity.setId(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(SevenBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(SevenBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(SevenBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}