class CapitalModel {
  String? country;
  String? city;

  CapitalModel({this.country, this.city});

  CapitalModel.fromJson(Map<String, dynamic> json) {
    country = json['country'];
    city = json['city'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['country'] = country;
    data['city'] = city;
    return data;
  }
}
