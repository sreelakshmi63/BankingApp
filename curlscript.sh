#!/bin/sh
echo 'Getting JWT Token'
token=$(curl "https://samplebankingapp.herokuapp.com/gettoken?custid=admin")
h1flag="Authorization: Bearer "
echo '1. Details of bank with ifscode BARB0CHIKAR'
echo '--------------------------------------------'
curl -H "${h1flag}${token}" "https://samplebankingapp.herokuapp.com/bankdetails?ifsccode=BARB0CHIKAR"
echo '2. Details of bank with ifscode BARB0CHIKAR and ANDB0002010'
echo '------------------------------------------------------------'
curl -H "${h1flag}${token}" "https://samplebankingapp.herokuapp.com/bankdetails?ifsccode=BARB0CHIKAR&ifsccode=ANDB0002010"
echo '3. Details of all ANDHRA BANK in AMALAPURAM city'
echo '-------------------------------------------------'
curl -H "${h1flag}${token}" "https://samplebankingapp.herokuapp.com/bankdetails?ifsccode=BARB0CHIKAR"
echo '4. Details of 2nd and 3rd ANDHRA BANK in AMALAPURAM city'
echo '---------------------------------------------------------'
curl -H "${h1flag}${token}" "https://samplebankingapp.herokuapp.com/bankdetails?bankname=ANDHRA%20BANK&city=AMALAPURAM&limit=2&offset=1"
echo '5. Details of all ANDHRA BANK and ANDHRA PRAGATHI GRAMEENA BANK in TANGUTUR city'
echo '---------------------------------------------------------------------------------'
curl -H "${h1flag}${token}" "https://samplebankingapp.herokuapp.com/bankdetails?bankname=ANDHRA%20BANK&bankname=ANDHRA%20PRAGATHI%20GRAMEENA%20BANK&city=TANGUTUR"
