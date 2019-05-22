<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<html>
<head>
    <base href="${pageContext.request.contextPath}/">
    <script type="text/javascript" src="Echarts/echarts.js"></script>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="sweetalert2/sweetalert2.js"></script>
    <link href="sweetalert2/sweetalert2.css" type="text/css" rel="stylesheet" />
    <script>
        $(function () {
            total = 0;
            json = [];
            stageList = [];
            $.ajax({
                url : "transaction/selectTransactionCountByStage.do",
                type : "get",
                data : {

                },
                dataType : "json",
                success : function (data) {
                    json = data.info;
                    total = data.total;
                    stageList = data.stage;
                    /*alert(json);
                    alert(stageList);
                    alert(total);*/

                },
                async : false
            });

           /* alert(json);
            alert(stageList);
            alert(total);*/

            echarts.init(document.getElementById('main')).setOption({

                title: {
                    text: '漏斗图',
                    subtext: '滴滴滴滴滴滴滴滴滴'
                },
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c}%"
                },
                toolbox: {
                    feature: {
                        dataView: {readOnly: false},
                        restore: {},
                        saveAsImage: {}
                    }
                },
                legend: {
                    data: stageList
                },
                calculable: true,
                series: [
                    {
                        name: '漏斗图',
                        type: 'funnel',
                        left: '10%',
                        top: 60,
                        //x2: 80,
                        bottom: 60,
                        width: '80%',
                        // height: {totalHeight} - y - y2,
                        min: 0,
                        max: total,
                        minSize: '0%',
                        maxSize: '100%',
                        sort: 'descending',
                        gap: 2,
                        label: {
                            show: true,
                            position: 'inside'
                        },
                        labelLine: {
                            length: 10,
                            lineStyle: {
                                width: 1,
                                type: 'solid'
                            }
                        },
                        itemStyle: {
                            borderColor: '#fff',
                            borderWidth: 1
                        },
                        emphasis: {
                            label: {
                                fontSize: 20
                            }
                        },
                        data:json
                    }
                ]
            });
        });

    </script>
</head>
<body>
<div id="main" style="width: 600px;height:400px;"></div>
</body>
</html>
