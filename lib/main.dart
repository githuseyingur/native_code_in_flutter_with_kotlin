import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:plugin_test/model/capital_model.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    SystemChrome.setEnabledSystemUIMode(SystemUiMode.immersiveSticky,
        overlays: [SystemUiOverlay.bottom]);
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Flutter Demo',
      theme: ThemeData(
        useMaterial3: false,
      ),
      home: const MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key});

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  static const platform = MethodChannel('random_channel');
  List<CapitalModel> capitalList = [];
  String base64String = '';

  Future<void> getCapitals() async {
    String jsonString;
    try {
      jsonString = await platform.invokeMethod('getJsonStringOfCapitals');
    } on PlatformException catch (e) {
      jsonString = '';
      print('select image in ablum : $e');
    }
    setState(() {
      final dataList = json.decode(jsonString) as List<dynamic>;
      capitalList = dataList
          .map((dynamic item) =>
              CapitalModel.fromJson(item as Map<String, dynamic>))
          .toList(); //        fromJson(jsonString as Map<String, dynamic>);
    });
  }

  Future<void> selectImageInAlbum() async {
    try {
      var tempUri = await platform.invokeMethod('selectImageInAlbum');

      setState(() {
        base64String = tempUri.toString();
      });

      print('URL : $tempUri');
    } on PlatformException catch (e) {
      print('select image in ablum : $e');
    }
  }

  Future<void> takePhoto() async {
    try {
      var cameraTempUri = await platform.invokeMethod('takePhoto');

      setState(() {
        base64String = cameraTempUri.toString();
      });

      print('URL : $cameraTempUri');
    } on PlatformException catch (e) {
      print('select image in ablum : $e');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 12),
        child: SingleChildScrollView(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.start,
            children: [
              const SizedBox(
                height: 40,
              ),
              Column(
                children: [
                  const Text(
                    'CAPITAL LIST',
                    style: TextStyle(fontSize: 18, fontWeight: FontWeight.w600),
                  ),
                  const SizedBox(
                    height: 10,
                  ),
                  Container(
                    height: MediaQuery.of(context).size.height * 0.60,
                    decoration: BoxDecoration(
                        border: Border.all(
                            color: Colors.grey[300] ?? Colors.grey, width: 1.2),
                        borderRadius: BorderRadius.circular(10)),
                    child: capitalList.isNotEmpty
                        ? ListView.builder(
                            itemCount: capitalList.length,
                            padding: const EdgeInsets.symmetric(
                                horizontal: 16, vertical: 20),
                            itemBuilder: (context, index) {
                              return Card(
                                color: Colors.blueGrey,
                                shape: RoundedRectangleBorder(
                                    borderRadius: BorderRadius.circular(12)),
                                child: Column(
                                  children: [
                                    const SizedBox(
                                      height: 5,
                                    ),
                                    Text(
                                      capitalList[index].city ?? '-',
                                      style: const TextStyle(
                                          color: Colors.white, fontSize: 14),
                                    ),
                                    Text(
                                      capitalList[index].country ?? '-',
                                      style: const TextStyle(
                                          color: Colors.white70, fontSize: 10),
                                    ),
                                    const SizedBox(
                                      height: 5,
                                    ),
                                  ],
                                ),
                              );
                            },
                          )
                        : Center(
                            child: Text(
                              'Please generate capital list',
                              style: TextStyle(
                                  color: Colors.blueGrey[300],
                                  fontSize: 11,
                                  fontWeight: FontWeight.w400),
                            ),
                          ),
                  ),
                ],
              ),
              const SizedBox(
                height: 20,
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  SizedBox(
                    width: MediaQuery.of(context).size.width * 0.42,
                    child: ElevatedButton(
                        style: ElevatedButton.styleFrom(
                          backgroundColor: Colors.deepPurpleAccent,
                        ),
                        onPressed: () async {
                          await getCapitals();
                        },
                        child: const Text(
                          'GENERATE CAPITAL LIST',
                          style: TextStyle(
                              fontSize: 11, fontWeight: FontWeight.w600),
                        )),
                  ),
                  const SizedBox(
                    width: 16,
                  ),
                  SizedBox(
                    width: MediaQuery.of(context).size.width * 0.42,
                    child: ElevatedButton(
                        style: ElevatedButton.styleFrom(
                          backgroundColor: Colors.blue,
                        ),
                        onPressed: () async {
                          showDialog(
                            context: context,
                            builder: (context) => AlertDialog(
                                title: const Text(
                                  'Choose Source',
                                  style: TextStyle(fontSize: 14),
                                  textAlign: TextAlign.center,
                                ),
                                content: Row(
                                  mainAxisAlignment:
                                      MainAxisAlignment.spaceAround,
                                  children: [
                                    ElevatedButton(
                                        onPressed: () async {
                                          await selectImageInAlbum();
                                          Navigator.of(context).pop();
                                        },
                                        child: const Text('GALLERY')),
                                    ElevatedButton(
                                        onPressed: () async {
                                          await takePhoto();
                                          Navigator.of(context).pop();
                                        },
                                        child: const Text('CAMERA')),
                                  ],
                                )),
                          );
                        },
                        child: const Text(
                          'PICK IMAGE',
                          style: TextStyle(
                              fontSize: 11, fontWeight: FontWeight.w600),
                        )),
                  ),
                ],
              ),
              const SizedBox(
                height: 10,
              ),
              Container(
                height: MediaQuery.of(context).size.height * 0.18,
                padding:
                    const EdgeInsets.symmetric(horizontal: 12, vertical: 12),
                decoration: BoxDecoration(
                    border: Border.all(
                        color: Colors.grey[300] ?? Colors.grey, width: 1.2),
                    borderRadius: BorderRadius.circular(10)),
                child: base64String != ''
                    ? Image.memory(
                        base64.decode(
                            base64String.replaceAll(RegExp(r'\s+'), '')),
                        fit: BoxFit.contain,
                      )
                    : Center(
                        child: Text(
                        'Please pick image',
                        style: TextStyle(
                            color: Colors.blueGrey[300],
                            fontSize: 10,
                            fontWeight: FontWeight.w400),
                      )),
              ),

              // Image.file(
              //   _getFileFromContentUri(uri),
              //   fit: BoxFit.cover,
              // ),
              // Image(
              //   image: _getImageProvider(uri),
              //   fit: BoxFit
              //       .cover, // İsteğe bağlı olarak, görüntüyü uygun şekilde boyutlandırabilirsiniz.
              // )
            ],
          ),
        ),
      ),
    );
  }
}
