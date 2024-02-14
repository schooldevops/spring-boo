# Mongo Index

## createIndex 로 인덱스 생성하기 

```mongodb-json
use project

db.project.createIndex(
    {'name': 1},
    {name: "nameindex"}  
)
```

- 1 값은 Ascending으로 소팅한다는 의미이다. 

## compoundIndex 로 복합 인덱스 생성하기 

```mongodb-json
use project

db.project.createIndex(
    {'name': 1, 'cost': 1},
    {name: "namecostindex"}
)
```