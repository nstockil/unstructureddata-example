# Introduction
These materials written for a workshop on unstructured data that was presented to college level group as part of their coursework in 2018.

The materials have been left as is and are not actively maintained.

# NoSQL

NoSQL is a data solution that does not have a formal data structure defined.

Consider a table in a relational database (eg. SQL database).  Before using it to store data, the table must be created.  The follow command creates and defines the structure of the data.

```sql
CREATE TABLE ExampleTable
(
    column1 int,
    column2 varchar(10),
    ...
)
```

In contrast, NoSQL solution is considered semi-structured.  It does not need to have the exact defintion of how the data looks defined in advanced.  This gives additional flexibility over relational data solutions.

While flexibility is increased with a semi-structured approach, the consistency of data is compromised.  That is the data stored in the solution may not be in the same format.

In cases of updating data schemas (known as a data migration), there is the potential that a change to the table structure will break a dependent service.  The flexibility of NoSQL means this is no longer a concern.

NoSQL solutions are often used for big data and real time applications.  Xbox as an example uses NoSQL solutions to store their data for leaderboard and game achievements information.

## Types of NoSQL
There are a few different types of NoSQL options.  Below are some of the most common.

### Key-Value stores
- Every item (value) is stored under a attribute.
- These attributes are known as Keys.

### Wide-Column stores
- Stores data in columns rather than rows.
- Optimized for queries over large datasets.

### Document databases
- Each key has a complex data type.
- This data type is known as a document.
- A document can be a set of attributes with values.

### Graph databases
- stores network information.
- eg. A network of followers on social media.

# NoSQL in Azure

Azure provides various NoSQL solutions.
- Azure Table Storage
- Azure Blob Storage
- CosmosDB

## Azure Table Storage
An Azure service to store NoSQL data in the cloud.  It provides a key base store with a schemaless design.

### Core Components
- A Table is considered a collection of entities.

- An Entity is a set of properties (similar to a row of data in a SQL table).

- A Property is a key-value pair.

### Entities
Every entity can have up to 255 properties associated with it.  There are three properties that every entity must have:
- Parition Key
    - Defines a high-level idenifier for an entity.
    - Think of it as defining the group an entity belongs to.
    - Parition keys are used to query groups of entities more efficiently.
- Row Key
    - Unique within a partition.
- Timestamp
    - Automatically added when adding a new entity.
    - Specifies the last update on the entity.

The Parition Key and the Row Key uniquely indentify an entity.

The maximum size a entity can be is 1MB.

# Working with Azure Table Storage

## Creating a Table Storage Service
1. Sign into the Azure Portal like in previous sessions.
2. Click Create a Resource in the side menu.
3. Search for Storage Account and select this option.
4. Fill in the form provide.  This will create a Storage Account where the data for the Table Storage will be held.

    | Field                    | Value                                                               |
    | ------------------------ | --------------------------------------------------------------------|
    | Deployment Model         | Resource manager                                                    |
    | Account Kind             | Storage (general purpose v2 )                                       |
    | Replication              | Locally Redundant                                                   |
    | Performance              | Standard                                                            |
    | Access Tier              | Hot                                                                 |
    | Secure transfer required | Yes                                                                 |
    | Subscription             | Your own student subscription                                       |
    | Resource Group           | Either a new one, or one you have been using for the course so far. |

5. Once created, view the resource.  This will give you all the options.
6. Go to the Table Service section and click Tables.
7. Click on +Table to create a Table Service.  You will be asked to provide a table name.  The example will be using used a Table called students.
8. *(Optional)* Install [Azure Storage Explorer](https://azure.microsoft.com/en-us/features/storage-explorer/).  This is a tool that lets you see inside the table storage.

You have now configured an Azure Table Storage solution.  It is ready to have data written to it.  Note how we did not have to specify a schema for our data like we could with SQL.

## Programming for Table Storage
It is possible to store and retrieve data from Table Storage using a variety of technologies including Java, Python, .NET and the OData protocol.

We will focus on Java in this session.

# Links

[Azure Table Storage Documention](https://docs.microsoft.com/en-us/azure/cosmos-db/table-storage-overview)

[Java and Azure Table Storage Sample from Azure Documentation](https://docs.microsoft.com/en-us/azure/cosmos-db/table-storage-how-to-use-java)

[Xbox Improves Services and Delivery Speed by Playing Games and Music in the Cloud](https://customers.microsoft.com/en-us/story/xbox-improves-services-and-delivery-speed-by-playing-g)
