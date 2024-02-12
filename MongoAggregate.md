# MongoDB Aggregate Query

## match / count

```mongodb-json
db.project.aggregate([
    {$match: {"cost": {"$gt": 2000}}},
    {$count: "costly_projects"}
])
```

## match / group / sum / sort 

```mongodb-json
db.project.aggregate([
    {$match: {"cost": {"$gt": 1000}}},
    {$group: {_id: "$startDate", total: {$sum: "$cost"}}},
    {$sort: {"total": -1}}
])
```

## lookpu / wind / unwind / project

```mongodb-json
db.project.aggregate([
    {$loopup: {
            from: "task",
            localField: "_id",
            foreignField: "pid",
            as: "ProjectTasks"
        }
    },
    {$unwind: "$ProjectTasks"},
    {$project: {
            _id: 1,
            name: 1,
            taskName: "$ProjectTasks.name",
            taskOwnerName: "$ProjectTasks.ownername"
        }
    }
])
```