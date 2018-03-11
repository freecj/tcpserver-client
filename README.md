1. G8RClient is a tcp client. You need ip addr, prot number, cookie file to start it.
2. G8RServer is a thread-pool server which need prot number and thread number to start it. And the server has Poll and Guess function.
1). Guess function, the guessed number is in the range of 0-9, if the client send the right answer, the score +1, otherwise the server will send the hint for the client send again.
2). if the parameters of the request from the client is not the format, or not the valid number  or the number of param is not 1, the server will stop the connection immediately.
The initial score is 0.
Function> Guess
Guess (0-9)? 5
Guess too low. Guess (0-9)? 7
Guess too low. Guess (0-9)? 9
Guess too high. Guess (0-9)? 8
Correct! Score 2

Here’s an example initial execution of the client:
Function> Poll
Name (First Last)> Bob
Poorly formed name. Name (First Last)> Bob Smith
Bob's food mood> Mexican
20% + 1% off at Tacopia
Note that the error above is from the G8R server.
 
Executing the client again, we get
Function> #$%#$
Bad user input: Function not a proper token (alphanumeric)
Function> Poll
Bob's Food mood> Italian
25% off at Pastastic
Note that the error above is from the (local) G8R serialization.

3. CookieList is a cookie class that has List of cookies (name/value pairs).
4. G8RMessage has G8RResponse and G8Rrequest kinds of messages.
Request:
G8R/1.0 Q RUN NameStep Bob Smith\r\n
\r\n
G8RResponse:
G8R/1.0 R OK FoodStep Bob’s Food mood>\r\n (Message part)
FName=Bob\r\n(Cookies part)
LName=Smith\r\n
\r\n
