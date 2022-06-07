package me.marti201.styletp.table;

import java.io.NotActiveException;
import java.util.UUID;

public class WrongOffTable extends OffTable{
    @Override
    public boolean isOffInTable(UUID p) throws Exception {
        throw new NotActiveException("Wrong database");
    }

    @Override
    public void setOffInTable(UUID p, boolean off) throws Exception {
        throw new NotActiveException("Wrong database");
    }
}
