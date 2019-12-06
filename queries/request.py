import requests

with open('search_strings.txt', 'r') as f:
    search_strings = f.read().splitlines()

query_string = """\
CALL db.index.fulltext.queryNodes('work', '{search_string}') \
YIELD node as work, score \
RETURN work.label, score, work.pagerank, sqrt(work.pagerank * score) as calc \
ORDER BY calc descending\
"""

data = {}
data['statements'] = []
for string in search_strings:
    formatted_string = query_string.format(search_string=string)
    data['statements'].append({'statement': formatted_string})

url = 'url:7474/db/data/transaction/commit'
r = requests.post(url, auth=('user', 'password'), json=data)
print(r.text)

