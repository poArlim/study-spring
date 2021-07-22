## Http methods
- get : 리소스 취득
- post : 리소스 생성, 쿼리 파라미터를 쓸 수 있지만 권장하지 않고 데이터는 DataBody 에 넣어서 보낸다. (보안)
- put : 리소스 갱신, 요청한 리소스가 없다면 생성. 마찬가지로 쿼리 파라미터를 권장하지 않고 DataBody 에 XML 이나 json 형태로 실어서 보낸다.
- delete : 리소스 삭제


## Response 작성하기 
- ResponseEntity 객체에 status(상태), body(데이터) 담아서 리턴

```java
public ResponseEntity<User> put(@RequestBody User user){
    return ResponseEntity.status(HttpStatus.CREATED).body(user);
}
```

## JSON

DataBody 를 주고받을 때 주로 XML, JSON 을 이용 -> 요즘은 주로 JSON 을 이용한다. 
key 와 value 의 쌍을 {} 로 묶어서 생성한다.

#### Data type 
- string : value
- number : value
- boolean : value
- object : value {}
- array : value []

```JSON
{
    "userID" : "steve",
    "account" : "steve@gmail.com",
    "age" : 24,
    
    "user" : {
        "phone_number" : "010-5555-5555",
        "address" : "seoul"
    }
    
    "user_list" : [
        {
            "phone_number" : "010-1234-5678",
            "address" : "seoul"
        },
        {
            "phone_number" : "010-4321-8765",
            "address" : "yang-ju"
        }
    ]
}
```
