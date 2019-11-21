
Jwt token will be expired after 5 days. For generating new token api is:
https://samplebankingapp.herokuapp.com/gettoken?custid=<custid>

Use the received token in other apis for getting bank details. For example in curl script,

curl -H "Authorization: Bearer <token>" "https://samplebankingapp.herokuapp.com/bankdetails?ifsccode=BARB0CHIKAR"


