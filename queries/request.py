from pylatex import Document, Section, Subsection, LongTable
from pylatex.utils import bold

import requests

with open('search_strings.txt', 'r') as f:
    search_strings = f.read().splitlines()

query_string = """\
CALL        db.index.fulltext.queryNodes('work', '{search_string}') \
YIELD       node as work, score \
RETURN      work.label, score, work.pagerank AS pagerank, \
            sqrt(work.pagerank * score) AS calc \
ORDER BY    calc descending \
LIMIT       10 \
"""

data = {}
data['statements'] = []
for string in search_strings:
    formatted_string = query_string.format(search_string=string)
    data['statements'].append({'statement': formatted_string})

uri = 'url:7474/db/data/transaction/commit'
r = requests.post(uri, auth=('username', 'password'), json=data)

with open('results.json', 'w') as f:
    f.write(r.text)

results = r.json()['results']

columns = results[0]['columns']

#doc = Document(fontenc=None,inputenc=None)
doc = Document()

with doc.create(Section('Tables')):
    for i in range(len(search_strings)):
        with doc.create(Subsection(search_strings[i])):
            with doc.create(LongTable('|'+'p{8cm}|'+'p{1cm}|'*(len(columns)-1))) as table:
                table.add_hline()
                table.add_row((*columns),mapper=bold)
                rows = results[i]['data']
                for row in rows:
                    table.add_hline()
                    label = row['row'].pop(0)
                    scores = [ round(float(elem), 4) for elem in row['row'] ]
                    table.add_row((label, *scores))
                table.add_hline()
doc.generate_pdf('tables', compiler='lualatex')
