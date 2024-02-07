# MongoDB Query Language Sample

## Project Name에 해당하는 대상 조회하기 

```
db.getCollection('project').find({"name":"ProjectA"})
```

- 위 내용은 project 컬렉션에서 필드 값이 ProjectA 라는 내용을 조회한다. 

## AND 조건 

```
db.getCollection('project').find({$and:[{"cost":3000},{"name":"ProjectA"}]})
```

- cost값이 3000이고, project.name값이 ProjectA을 동시에 만족하는 조건에 대한 Documents 조회 

## OR 조건

```
db.getCollection('project').find({$or:[{"cost":3000},{"name":"ProjectA"}]})
```

- cost값이 3000이고, project.name값이 ProjectA중 둘중 하나만 만족하는 조건에 대한 Documents 조회 

## Greate Than 조건 

```
db.getCollection('project').find({"cost":{$gt:2500}})
```

- cost 값이 2500보다 큰 Documents를 조회한다. 


## 복합 조건 이용하기 

```
db.getCollection('project').find({"cost":{$gt:2500},$or:[{"name":"ProjectB"},{"name":"ProjectA"}]})
```

- cost값이 2500보다 크고, project.name이 ProjectB 혹은 ProjectA 인경우에 해당하는 Documents를 조회한다. 


## Not 조건 

```
db.getCollection('project').find({"cost":{ $not: { $gt: 2500} } })
```

- cost 값이 2500이 아닌 Documents 조회 

## IN 조건 

```
db.getCollection('project').find( { countryList: { $in: [ "USA", "UK" ]} } )
```

- countryList 가 "USA", "UK"에 포함되는 Documents 를 조회한다. 

## 관련 레퍼런스 

https://docs.mongodb.com/manual/tutorial/query-documents/


