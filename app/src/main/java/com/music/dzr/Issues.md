Что стоит сделать:
1. Сделать src kotlin а не java

Над чем стоит задуматься:
1. Разместить ли Market в library:market?
2. Нужен ли Proguard в модулях?
3. Нужно ли перенести core:common содержимое в core.common пакет?
4. Многие data классы имеют open-свойства, нужно ли делать final в угоду правильности кода?
5. Стоит из RemoteDataSource возвращать Result вместо NetworkResponse