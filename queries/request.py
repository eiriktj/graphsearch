from pylatex import Document, Section, Subsection, Tabular

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
r = requests.post(url, auth=('username', 'password'), json=data)

with open('results.json', 'w') as f:
    f.write(r.text)

results = r.json()['results']

columns = results[0]['columns']

doc = Document(fontenc=None,inputenc=None)

with doc.create(Section('Tables')):
    for i in range(len(search_strings)):
        with doc.create(Subsection(search_strings[i])):
            with doc.create(Tabular('|'+'l|'*len(columns))) as table:
                table.add_hline()
                table.add_row((*columns))
                rows = results[i]['data']
                for row in rows:
                    table.add_hline()
                    table.add_row((*row['row']))
                table.add_hline()
doc.generate_pdf('tables', compiler='lualatex')
