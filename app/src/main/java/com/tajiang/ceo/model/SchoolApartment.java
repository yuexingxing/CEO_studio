package com.tajiang.ceo.model;

import java.util.List;

/**
 * Created by work on 2016/7/13.
 */
public class SchoolApartment {


    /**
     * id : 1vfgu
     * name : 浙江财经大学
     * zonesList : [{"id":172,"schoolId":"1vfgu","name":"桃李苑","createdAt":1458026286000,"updatedAt":1458026287000,"apartmentList":[{"id":147,"schoolId":"1vfgu","name":"1幢","createdAt":1450326100000,"updatedAt":1450326102000,"zonesId":172},{"id":148,"schoolId":"1vfgu","name":"2幢","createdAt":1450326137000,"updatedAt":1450326138000,"zonesId":172},{"id":149,"schoolId":"1vfgu","name":"3幢","createdAt":1450326153000,"updatedAt":1450326154000,"zonesId":172},{"id":150,"schoolId":"1vfgu","name":"4幢","createdAt":1450326169000,"updatedAt":1450326172000,"zonesId":172},{"id":151,"schoolId":"1vfgu","name":"5幢","createdAt":1450326181000,"updatedAt":1450326183000,"zonesId":172},{"id":152,"schoolId":"1vfgu","name":"6幢","createdAt":1450326194000,"updatedAt":1450326196000,"zonesId":172},{"id":153,"schoolId":"1vfgu","name":"7幢","createdAt":1450326208000,"updatedAt":1450326210000,"zonesId":172},{"id":154,"schoolId":"1vfgu","name":"8幢","createdAt":1450326219000,"updatedAt":1450326221000,"zonesId":172},{"id":155,"schoolId":"1vfgu","name":"9幢","createdAt":1450326231000,"updatedAt":1450326233000,"zonesId":172},{"id":156,"schoolId":"1vfgu","name":"10幢","createdAt":1450326242000,"updatedAt":1450326245000,"zonesId":172},{"id":157,"schoolId":"1vfgu","name":"11幢","createdAt":1450326254000,"updatedAt":1450326256000,"zonesId":172},{"id":158,"schoolId":"1vfgu","name":"12幢","createdAt":1450326264000,"updatedAt":1450326266000,"zonesId":172},{"id":159,"schoolId":"1vfgu","name":"13幢","createdAt":1450326275000,"updatedAt":1450326277000,"zonesId":172}]},{"id":174,"schoolId":"1vfgu","name":"桂花苑","createdAt":1458100195000,"updatedAt":1458100197000,"apartmentList":[{"id":160,"schoolId":"1vfgu","name":"14幢","createdAt":1450326286000,"updatedAt":1450326288000,"zonesId":174},{"id":161,"schoolId":"1vfgu","name":"15幢","createdAt":1450326296000,"updatedAt":1450326298000,"zonesId":174},{"id":162,"schoolId":"1vfgu","name":"16幢","createdAt":1450326306000,"updatedAt":1450326308000,"zonesId":174},{"id":163,"schoolId":"1vfgu","name":"17幢","createdAt":1450326319000,"updatedAt":1450326322000,"zonesId":174},{"id":164,"schoolId":"1vfgu","name":"18幢","createdAt":1450326331000,"updatedAt":1450326333000,"zonesId":174},{"id":165,"schoolId":"1vfgu","name":"19幢","createdAt":1450326341000,"updatedAt":1450326343000,"zonesId":174},{"id":166,"schoolId":"1vfgu","name":"20幢","createdAt":1450326352000,"updatedAt":1450326354000,"zonesId":174},{"id":167,"schoolId":"1vfgu","name":"21幢","createdAt":1450326362000,"updatedAt":1450326364000,"zonesId":174},{"id":168,"schoolId":"1vfgu","name":"22幢","createdAt":1450326380000,"updatedAt":1450326381000,"zonesId":174},{"id":169,"schoolId":"1vfgu","name":"23幢","createdAt":1450326390000,"updatedAt":1450326391000,"zonesId":174},{"id":170,"schoolId":"1vfgu","name":"24幢","createdAt":1450326400000,"updatedAt":1450326402000,"zonesId":174},{"id":171,"schoolId":"1vfgu","name":"25幢","createdAt":1450326411000,"updatedAt":1450326413000,"zonesId":174}]}]
     */

    private String id;
    private String name;
    /**
     * id : 172
     * schoolId : 1vfgu
     * name : 桃李苑
     * createdAt : 1458026286000
     * updatedAt : 1458026287000
     * apartmentList : [{"id":147,"schoolId":"1vfgu","name":"1幢","createdAt":1450326100000,"updatedAt":1450326102000,"zonesId":172},{"id":148,"schoolId":"1vfgu","name":"2幢","createdAt":1450326137000,"updatedAt":1450326138000,"zonesId":172},{"id":149,"schoolId":"1vfgu","name":"3幢","createdAt":1450326153000,"updatedAt":1450326154000,"zonesId":172},{"id":150,"schoolId":"1vfgu","name":"4幢","createdAt":1450326169000,"updatedAt":1450326172000,"zonesId":172},{"id":151,"schoolId":"1vfgu","name":"5幢","createdAt":1450326181000,"updatedAt":1450326183000,"zonesId":172},{"id":152,"schoolId":"1vfgu","name":"6幢","createdAt":1450326194000,"updatedAt":1450326196000,"zonesId":172},{"id":153,"schoolId":"1vfgu","name":"7幢","createdAt":1450326208000,"updatedAt":1450326210000,"zonesId":172},{"id":154,"schoolId":"1vfgu","name":"8幢","createdAt":1450326219000,"updatedAt":1450326221000,"zonesId":172},{"id":155,"schoolId":"1vfgu","name":"9幢","createdAt":1450326231000,"updatedAt":1450326233000,"zonesId":172},{"id":156,"schoolId":"1vfgu","name":"10幢","createdAt":1450326242000,"updatedAt":1450326245000,"zonesId":172},{"id":157,"schoolId":"1vfgu","name":"11幢","createdAt":1450326254000,"updatedAt":1450326256000,"zonesId":172},{"id":158,"schoolId":"1vfgu","name":"12幢","createdAt":1450326264000,"updatedAt":1450326266000,"zonesId":172},{"id":159,"schoolId":"1vfgu","name":"13幢","createdAt":1450326275000,"updatedAt":1450326277000,"zonesId":172}]
     */

    private List<ApartmentZone> zonesList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ApartmentZone> getZonesList() {
        return zonesList;
    }

    public void setZonesList(List<ApartmentZone> zonesList) {
        this.zonesList = zonesList;
    }


}
