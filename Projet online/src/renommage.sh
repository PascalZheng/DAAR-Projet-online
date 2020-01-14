for file in *.txt
do
    titre=$(sed -n '/Title/,/Author/{/Author/d;p}' $file | awk -F "Title: " '{for (i=2; i<NF; i++) printf $i " "; print $NF}' | sed 's/[^a-zA-Z0-9 ]//g')
    mv "$file" "$titre.txt"
done
