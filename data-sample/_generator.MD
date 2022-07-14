https://json-generator.com/

```
[
  '{{repeat(5, 7)}}',
  {
    event_id: '{{objectId()}}',
    log_id: '{{objectId()}}',
    date: '{{date(new Date(2022, 6, 1), new Date(), "YYYY-MM-ddThh:mm:ss Z")}}',
    source: '{{lorem(2, "words")}}',
    env: 'production',
    session_id: '{{objectId()}}',
    session_key: '{{random("K1", "K2", "K3")}}',
    session_value: function (tags) {
      if (this.session_key == 'K1')
        return '{' + 
          '\"var1\": \"' + tags.lorem(1, 'sentences') + '\",' + 
          '\"var2\": \"' + tags.lorem(1, 'words') + '\",' + 
          '\"var3\": ' + tags.integer(1, 5) + 
          '}'
      else if (this.session_key == 'K2')
        return '{' + 
          '\"list1\": [' + tags.integer(1, 15) + ', ' + tags.integer(1, 5) + '],' +
          '\"name\": \"' + tags.surname() + '\"' +
          '}'
      else
        return tags.lorem(1, 'paragraphs')
    },
    user_question: '{{lorem(10, "words")}}',
    id_content: '{{integer(1, 99999999)}}'
  }
]

```