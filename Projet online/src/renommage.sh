for file in *.txt.utf-8
do
    titre=$(sed -n '/Title/,/Author/{/Author/d;p}' $file | cut -d$'\n' -f1 | awk -F "Title: " '{print $2}')
    mv "$file" "$titre.txt"
done
