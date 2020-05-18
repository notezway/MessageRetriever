# MessageRetriever
Simple android application for educational purposes.

Приложение читает файлы из http://messageretriver.ntzw.ru/endpoint/{PAGE}.json, где {PAGE} - номер страницы.
Каждая страница содержит список сообщений и имеет следующую структуру:
```JSON
[
  {
    "id": "7e3122f9-6d50-44bc-b217-44eb7ec5f4ae",
    "time": 1463451777180,
    "text": "mz4Snvvtqui3iuVhratnkTuDCejxme4AudtzifsyLszb8bpxmszigepWvgYgqybs8HAGluwcTUescikwapmjbppltvYboy9g"
  },
  {
    "id": "a2c03a67-1942-456e-vc80-455b5f015af0",
    "time": 1463454782276,
    "text": "TRmxoqYyb0AgfrcxbbhjijcqXWkzkto5lGcb5vlp3dQusAgnvdk82N7kajfOsmdlnkhmL8dkdkv"
  },
…
]
```
Каждая страница содержит от 0 до 50 сообщений включительно.
Приложение читает из страниц сообщения и показывает их пользователю.
Пользователь может удалить любое сообщение из списка свайпом.
