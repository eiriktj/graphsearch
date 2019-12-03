import requests
import json

with open('queries.json', 'r') as f:
    data = json.load(f)
url = 'url:7474/db/data/transaction/commit'
r = requests.post(url, auth=('user', 'password'), json=data)
print(r.text)
