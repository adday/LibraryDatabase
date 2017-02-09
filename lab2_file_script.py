import csv

def convert_date(date):
		part = date.split("/")
		return part[2].strip() + "-" + part[0].strip() + "-" + part[1].strip()
		
		
out = open("lab2_insert.sql","w")

author_id = []
author_names = []
author_phone_numbers =[]

with open("authors.csv") as f:
	r = csv.reader(f)
	for row in r:
		ncol=len(row)
		id = row[0].strip()
		author_id.append(id)
		author_names.append(row[1].split())
		for j in range(2,ncol):
			raw = row[j].split("(")
			if 'None' in raw[0]: continue
			num, type = raw[0].replace("-",""), raw[1].rstrip(")")
			author_phone_numbers.append([id,num.strip(),type])
			
out.write("INSERT INTO Author VALUES ")
for i in range(len(author_id)):
	if i == len(author_id)-1:
		out.write("(" + str(author_id[i]) + ",'" + str(author_names[i][1]) + "','" + str(author_names[i][0]) + "');")
	else:
		out.write("(" + str(author_id[i]) + ",'" + str(author_names[i][1]) + "','" + str(author_names[i][0]) + "'),")
		
out.write("\nINSERT INTO Author_phone VALUES ")
for i in range(len(author_phone_numbers)):
	if('None' in author_phone_numbers[i][1]): continue
	if i == len(author_phone_numbers)-1:
		out.write("(" + str(author_phone_numbers[i][0]) + ",'" + str(author_phone_numbers[i][1]) + "');")
	else:
		out.write("(" + str(author_phone_numbers[i][0]) + ",'" + str(author_phone_numbers[i][1]) + "'),")

out.write("\nINSERT IGNORE INTO Phone VALUES ")
for i in range(len(author_phone_numbers)):
	if('None' in author_phone_numbers[i][1]): continue
	if i == len(author_phone_numbers)-1:
		out.write("('" + str(author_phone_numbers[i][1]) + "','" + str(author_phone_numbers[i][2]) + "');")
	else:
		out.write("('" + str(author_phone_numbers[i][1]) + "','" + str(author_phone_numbers[i][2]) + "'),")
		
ISBNs = []
titles = []
publisher = []
yr_published = []
book_author = []
			
with open("books.csv") as f:
	r = csv.reader(f)
	i = 0
	for row in r:
		if i%2 == 0:
			ISBN = row[0].replace("-","")
			ISBNs.append(ISBN)
			titles.append(row[4].strip())
			publisher.append(row[5].strip())
			yr_published.append(row[6][len(row[6])-4:])
		else:
			ncol=len(row)
			for j in range(ncol):
				id = row[j]
				book_author.append([ISBN,id])
		i = (i+1)%2

out.write("\nINSERT INTO Book VALUES ")
for i in range(len(ISBNs)):
	if i == len(ISBNs)-1:
		out.write("('" + str(ISBNs[i]) + "','" + str(titles[i]) + "'," + str(yr_published[i]) + "," + str(publisher[i]) + ");")
	else:
		out.write("('" + str(ISBNs[i]) + "','" + str(titles[i]) + "'," + str(yr_published[i]) + "," + str(publisher[i]) + "),")

out.write("\nINSERT INTO Written_by VALUES ")
for i in range(len(book_author)):
	if i == len(book_author)-1:
		out.write("('" + str(book_author[i][0]) + "'," + str(book_author[i][1]) + ");")
	else:
		out.write("('" + str(book_author[i][0]) + "'," + str(book_author[i][1]) + "),")
								
			
member_id = []
member_names = []
DOB = []

ISBNs = []
checked_out_by = []
checkout_date = []
checkin_date = []

with open("members.csv") as f:
	r = csv.reader(f)
	for row in r:
		if not " " in row[0]:
			id = row[0].strip()
			member_id.append(id)
			member_names.append(row[1].split())
			DOB.append(convert_date(row[3].strip()))
		else:
			ncol=len(row)
			ISBN = row[0].replace("-","")
			ISBNs.append(ISBN.strip())
			checked_out_by.append(id)
			checkout_date.append(convert_date(row[1].strip()))
			if ncol == 3: checkin_date.append(convert_date(row[2].strip()))
			else: checkin_date.append("9999-1-1")
			
out.write("\nINSERT INTO Member VALUES ")
for i in range(len(member_id)):
	if i == len(member_id)-1:
		out.write("(" + str(member_id[i]) + ",'" + str(member_names[i][1]) + "','" + str(member_names[i][0]) + "','" + str(DOB[i]) + "');")
	else:
		out.write("(" + str(member_id[i]) + ",'" + str(member_names[i][1]) + "','" + str(member_names[i][0]) + "','" + str(DOB[i]) + "'),")

out.write("\nINSERT INTO Borrowed VALUES ")
for i in range(len(ISBNs)):
	if i == len(ISBNs)-1:
		out.write("('" + str(ISBNs[i].strip()) + "'," + str(checked_out_by[i]) + ",'" + str(checkout_date[i]) + "','" + str(checkin_date[i]) + "');")
	else:
		out.write("('" + str(ISBNs[i].strip()) + "'," + str(checked_out_by[i]) + ",'" + str(checkout_date[i]) + "','" + str(checkin_date[i]) + "'),")

publisher_id = []
publisher_names = []
publisher_phone_numbers = []

with open("publisher.csv") as f:
	r = csv.reader(f)
	for row in r:
		ncol=len(row)
		id = row[0].strip()
		publisher_id.append(id)
		publisher_names.append(row[1].strip())
		for j in range(2,ncol):
			raw = row[j].split("(")
			if 'None' in raw[0]: continue
			num, type = raw[0].replace("-",""), raw[1].rstrip(")")
			publisher_phone_numbers.append([id,num.strip(),type])	

out.write("\nINSERT INTO Publisher VALUES ")
for i in range(len(publisher_id)):
	if i == len(publisher_id)-1:
		out.write("(" + str(publisher_id[i]) + ",'" + str(publisher_names[i]) + "');")
	else:
		out.write("(" + str(publisher_id[i]) + ",'" + str(publisher_names[i]) + "'),")

out.write("\nINSERT INTO Publisher_phone VALUES ")
for i in range(len(publisher_phone_numbers)):
	if('None' in publisher_phone_numbers[i][1]): continue
	if i == len(publisher_phone_numbers)-1:
		out.write("(" + str(publisher_phone_numbers[i][0]) + ",'" + str(publisher_phone_numbers[i][1]) + "');")
	else:
		out.write("(" + str(publisher_phone_numbers[i][0]) + ",'" + str(publisher_phone_numbers[i][1]) + "'),")

out.write("\nINSERT IGNORE INTO Phone VALUES ")
for i in range(len(publisher_phone_numbers)):
	if('None' in publisher_phone_numbers[i][1]): continue
	if i == len(publisher_phone_numbers)-1:
		out.write("('" + str(publisher_phone_numbers[i][1]) + "','" + str(publisher_phone_numbers[i][2]) + "');")
	else:
		out.write("('" + str(publisher_phone_numbers[i][1]) + "','" + str(publisher_phone_numbers[i][2]) + "'),")
		
out.close()
