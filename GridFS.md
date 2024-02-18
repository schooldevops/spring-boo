# GridFS 

- MongoDB GridFS는 매우 큰 파일을 위한 강력한 솔루션이다. 

## GridFS 소개:

- GridFS는 MongoDB에서 제공하는 파일 저장 시스템으로, BSON 문서 크기 제한(16MB)을 뛰어넘는 매우 큰 파일을 효율적으로 관리하기 위한 기능입니다. GridFS는 파일을 여러 조각으로 나누어 chunks라는 별도의 문서로 저장하여 데이터베이스 성능과 확장성을 유지한다. 

## GridFS 구조:

### 1. Bucket:

- 파일  그룹을 위한 논리적 컨테이너
- 여러 파일을 하나의 버킷에 저장 가능
- 버킷 이름은 문자열이며, 컬렉션 이름과 유사하게 작동

### 2. Chunk:

- 실제 파일 데이터를 저장하는 문서
- 기본 크기는 255KB, 최대 4MB까지 설정 가능
- 파일 크기에 따라 여러 개의 chunk 생성

### 3. Files Collection:

- 파일 메타데이터를 저장하는 컬렉션
- 파일 이름, 크기, 업로드 날짜, MIME 유형 등을 포함
- _id 필드는 chunk를 식별하는 키로 사용

### 4. Chunks Collection:

- 파일의 chunk 데이터를 저장하는 컬렉션
- _id 필드는 chunk 식별자, files collection _id와 연결
- chunk 데이터는 바이너리 형식으로 저장

## GridFS 아키텍처:

- GridFS 아키텍처: [[유효하지 않은 URL 삭제됨]]([유효하지 않은 URL 삭제됨])

## GridFS 효과:

- 큰 파일 저장: BSON 문서 크기 제한 없이 매우 큰 파일 저장 가능
- 효율적인 저장: 파일을 chunk로 나누어 저장하여 성능 향상
- 확장성: 여러 서버에 걸쳐 파일 분산 저장 가능
- 파일 관리: 메타데이터 관리, 버전 관리, 쿼리 등 다양한 기능 제공
- 간편한 사용: API를 통해 쉽게 파일 업로드, 다운로드, 관리 가능

## GridFS 활용 분야:

- 이미지, 동영상, 음악 등 멀티미디어 파일 저장
- 로그 파일, 백업 파일, 문서 파일 등 일반 파일 저장
- 콘텐츠 관리 시스템(CMS)
- 파일 공유 시스템

## 주의 사항:

- GridFS는 NoSQL 데이터베이스 기능이며, SQL 기반 관계형 데이터베이스와 다르게 작동한다.
- GridFS는 파일 시스템과 유사하지만, 모든 기능을 제공하지는 않는다.
- GridFS 사용 전에 공식 문서를 참고하여 구조, 아키텍처, 사용법 등을 숙지하는 것이 중요하다.
- GridFS는 매우 큰 파일을 MongoDB에 효율적으로 저장하고 관리할 수 있는 강력한 솔루션이다. 
- 다양한 분야에서 활용될 수 있으며, 사용하기 쉽고 확장성이 뛰어난 장점을 가지고 있다.

## 사용하기 

- MongoDB 용 툴이나 콘솔을 사용하여 GridFS를 사용하는 방법을 명령어와 함께 상세하게 설명

### 1. 몽고셸(Mongo Shell) 사용:

#### 1.1. 파일 업로드:

```mongodb-json
mongosh> use my_database
switched to db my_database
mongosh> db.fs.files.insert({filename: "my_file.txt", contentType: "text/plain"})
{
  "_id": {
    "$oid": "63e692272345678901234567"
  },
  "length": 123,
  "chunkSize": 255000,
  "uploadDate": {
    "$date": "2023-12-01T15:09:59.334Z"
  },
  "md5": "e3b0c44298fc1c149afbf4c8996fb924"
}
mongosh> db.fs.chunks.insert({files_id: { "$oid": "63e692272345678901234567" }, n: 0, data: BinData(0, "This is my file content.")})
{
  "_id": {
    "$oid": "63e692272345678901234568"
  },
  "files_id": {
    "$oid": "63e692272345678901234567"
  },
  "n": 0,
  "data": BinData(0, "This is my file content.")
}
```

#### 1.2. 파일 다운로드:

```mongodb-json
mongosh> use my_database
switched to db my_database
mongosh> var fileId = "63e692272345678901234567";
mongosh> var file = db.fs.files.findOne({"_id": fileId});
mongosh> var chunks = db.fs.chunks.find({"files_id": fileId}).sort({n: 1});
mongosh> var data = "";
while (chunk = chunks.next()) {
  data += chunk.data.readBinary();
}
mongosh> print(data);
This is my file content.
```

#### 1.3. 파일 삭제:

```mongodb-json
mongosh> use my_database
switched to db my_database
mongosh> db.fs.files.deleteOne({"_id": "63e692272345678901234567"});
{
  "acknowledged": true,
  "deletedCount": 1
}
mongosh> db.fs.chunks.deleteMany({"files_id": "63e692272345678901234567"});
{
  "acknowledged": true,
  "deletedCount": 1
}
```
