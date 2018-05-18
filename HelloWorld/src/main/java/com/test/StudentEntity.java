package com.test;

import com.microsoft.azure.storage.table.TableServiceEntity;

/**
 * The StudentEntity class defines the properties for an entity representing a student.
 * 
 * This class extends the TableServiceEntity class.
 * Doing this automatically provides the partitionkey, rowkey and timestamp properties for the storage entity.
 * If creating your own unique entity you must specify that it is a public class for it to work.
 */
public class StudentEntity extends TableServiceEntity
{
    public StudentEntity(String name, int mark, String grade, String email) 
    {
        this.partitionKey = grade;
        this.rowKey = email;
        this.studentname = name;
        this.studentMark = mark;
    }
    
    public StudentEntity() { }

    public String email;
    public int studentMark;
    public String studentname;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName(){
        return this.studentname;
    }

    public void setName(String name){
        this.studentname = name;
    }

    public int getStudentMark(){
        return this.studentMark;
    }

    public void setStudentMark(int mark){
        this.studentMark = mark;
    }
}