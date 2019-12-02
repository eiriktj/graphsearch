import requests
import json

with open('queries.json', 'r') as f:
    #data = f.read()
    data = json.load(f)
url = 'url:7474/db/data/cypher/'
headers = {'Content-type': 'application/json ', 'Accept': 'application/json;charset=UTF-8 '}
r = requests.post(url, auth=('user', 'password'), headers=headers, data=data)
print(r.text)
print(r.status_code)
print(data)
print(type(data))
