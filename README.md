# Hunter Six - Java Spring RESTful API Test

## How to build
```./gradlew clean build```

## How to test
```./gradlew test```

## Exercises
### Exercise 1
Make the tests run green (there should be one failing test)

Solution: Add static keyword to counter variable in Person class

### Exercise 2
Update the existing `/person/{lastName}/{firstName}` endpoint to return an appropriate RESTful response when the requested person does not exist in the list
- prove your results

Solution : Exception thrown when not found and handled through exception handler.
Tests : 
    shouldReturnErrorFromServiceWhenNotFound in PersonControllerTest
    shouldReturnPersonNotFound in HttpRequestTest

### Exercise 3
Write a RESTful API endpoint to retrieve a list of all people with a particular surname
- pay attention to what should be returned when there are no match, one match, multiple matches
- prove your results

Solution: New Get endpoint created
Tests : 
    PersonControllerTest :  shouldReturnPersonsWithSurnameFromService  , shouldReturnOnePersonWithSurnameFromService , 
                            shouldReturnNotFoundPersonWithSurnameIsNotfound 
    HttpRequestTest : shouldReturnPersonDetailsForSurname , shouldReturnNotFoundPersonWithSurnameNotFound

### Exercise 4
Write a RESTful API endpoint to add a new value to the list
- pay attention to what should be returned when the record already exists
- prove your resutls

Solution: New post endpoint created
Tests : 
    PersonControllerTest :  testAddPersonSuccessful  , testAddExistingPersonIsNotSuccessful , testAddPersonNotSuccessful 
    HttpRequestTest : shouldReturnSuccessWhenNewPersonAdded , shouldReturnErrorWhenAddingExistingPerson