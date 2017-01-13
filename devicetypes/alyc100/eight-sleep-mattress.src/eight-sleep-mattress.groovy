/**
 *  Eight Sleep Mattress
 *
 *  Copyright 2017 Alex Lee Yuk Cheung
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *	VERSION HISTORY
 *	13.01.2017: 1.0 BETA Release 6 - Changes to bed presence contact behaviour.
 *								   - Handle scenario of no partner credentials. 
 *	13.01.2017: 1.0 BETA Release 5 - Historical sleep chart improvements showing SleepScore.
 *	12.01.2017: 1.0 BETA Release 4c - Better 'Offline' detection and handling.
 *	12.01.2017: 1.0 BETA Release 4b - Minor event messaging improvements.
 *	12.01.2017: 1.0 BETA Release 4 - Further refinements to bed presence contact behaviour.
 *	11.01.2017: 1.0 BETA Release 3c - Use Google Chart image API for Android support
 *	11.01.2017: 1.0 BETA Release 3b - Further Chart formatting update
 *	11.01.2017: 1.0 BETA Release 3 - Chart formatting update
 								   - Attempt to improve bed detection
 *	11.01.2017: 1.0 BETA Release 2 - Change set level behaviour
 *								   - Support partner sleep trend data
 *                                 - Timer display changes
 *                                 - Add slider control on main tile
 *                                 - Many bug fixes
 *	11.01.2017: 1.0 BETA Release 1 - Initial Release
 */
metadata {
	definition (name: "Eight Sleep Mattress", namespace: "alyc100", author: "Alex Lee Yuk Cheung") {
		capability "Actuator"
		capability "Contact Sensor"
		capability "Polling"
		capability "Refresh"
		capability "Sensor"
		capability "Switch"
		capability "Switch Level"
        
        command "setHeatDuration"
        command "heatingDurationDown"
        command "heatingDurationUp"
        command "levelUp"
        command "levelDown"
        command "setNewLevelValue"
	}


	simulator {
		// TODO: define status and reply messages here
	}

	tiles (scale: 2){
		multiAttributeTile(name:"switch", type: "generic", width: 6, height: 6, canChangeIcon: true){
            tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
                attributeState "on", label:'${name}', action:"switch.off", icon:"st.Bedroom.bedroom12", backgroundColor:"#79b821", nextState:"turningOff"
                attributeState "off", label:'${name}', action:"switch.on", icon:"st.Bedroom.bedroom12", backgroundColor:"#ffffff", nextState:"turningOn"
                attributeState "turningOn", label:'${name}', action:"switch.off", icon:"st.Bedroom.bedroom12", backgroundColor:"#79b821", nextState:"turningOff"
                attributeState "turningOff", label:'${name}', action:"switch.on", icon:"st.Bedroom.bedroom12", backgroundColor:"#ffffff", nextState:"turningOn"
            	attributeState "offline", label:'${name}', icon:"st.Bedroom.bedroom12", backgroundColor:"#ff0000"
            }
            tileAttribute ("device.desiredLevel", key: "SLIDER_CONTROL") {
              attributeState "desiredLevel", action:"setNewLevelValue", range:"(10..100)"
            }
            tileAttribute ("timer", key: "SECONDARY_CONTROL") {
				attributeState "timer", label:'${currentValue} left'
			}
        }
        
        standardTile("presence", "device.contact", width: 2, height: 2) {
			state("closed", label:'In Bed', icon:"st.Bedroom.bedroom2", backgroundColor:"#79b821")
			state("open", label:'Out Of Bed', icon:"st.Bedroom.bedroom6", backgroundColor:"#ffa81e")
		}
        
        standardTile("switch_mini", "device.switch", width: 2, height: 2) {
        	state( "on", label:'${name}', action:"switch.off", icon:"st.Bedroom.bedroom12", backgroundColor:"#79b821")
            state( "off", label:'${name}', action:"switch.on", icon:"st.Bedroom.bedroom12", backgroundColor:"#ffffff")
        }
        
        valueTile("currentHeatLevel", "device.currentHeatLevel", width: 2, height: 2){
			state "default", label: '${currentValue}', unit:"%", 
            backgroundColors:[
				[value: 0, color: "#153591"],
				[value: 10, color: "#44b621"],
				[value: 30, color: "#f1d801"],
				[value: 60, color: "#d04e00"],
				[value: 100, color: "#bc2323"]
			]
		}
        
        standardTile("network", "device.network", width: 2, height: 2, inactiveLabel: false, canChangeIcon: false) {
			state ("default", label:'unknown', icon: "st.unknown.unknown.unknown")
			state ("Connected", label:'Online', icon: "st.Health & Wellness.health9", backgroundColor: "#79b821")
			state ("Not Connected", label:'Offline', icon: "st.Health & Wellness.health9", backgroundColor: "#bc2323")
		}
        
		standardTile("refresh", "device.switch", width: 2, height: 2, inactiveLabel: false, canChangeIcon: false, decoration: "flat") {
			state("default", label:'refresh', action:"refresh.refresh", icon:"st.secondary.refresh-icon")
		}
        
        valueTile("heatingDuration", "device.heatingDuration", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state("default", label:'${currentValue}')
		}
        
        valueTile("level", "device.desiredLevel", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
            state "default", label: '${currentValue}%', unit:"%", 
            backgroundColors:[
				[value: 0, color: "#153591"],
				[value: 10, color: "#44b621"],
				[value: 30, color: "#f1d801"],
				[value: 60, color: "#d04e00"],
				[value: 100, color: "#bc2323"]
			]
        }
        
        standardTile("levelUp", "device.levelUp", width: 1, height: 1, canChangeIcon: false, inactiveLabel: false, decoration: "flat") {
			state "levelUp", label:'  ', action:"levelUp", icon:"st.thermostat.thermostat-up", backgroundColor:"#ffffff"
		}

		standardTile("levelDown", "device.levelDown", width: 1, height: 1, canChangeIcon: false, inactiveLabel: false, decoration: "flat") {
			state "levelDown", label:'  ', action:"levelDown", icon:"st.thermostat.thermostat-down", backgroundColor:"#ffffff"
		}
        
        valueTile("status", "device.status", inactiveLabel: false, decoration: "flat", width: 4, height: 1) {
			state("default", label:'${currentValue}')
		}
        
        standardTile("heatingDurationUp", "device.heatingDurationUp", width: 1, height: 1, canChangeIcon: false, inactiveLabel: false, decoration: "flat") {
			state "heatingDurationUp", label:'  ', action:"heatingDurationUp", icon:"st.thermostat.thermostat-up", backgroundColor:"#ffffff"
		}

		standardTile("heatingDurationDown", "device.heatingDurationDown", width: 1, height: 1, canChangeIcon: false, inactiveLabel: false, decoration: "flat") {
			state "heatingDurationDown", label:'  ', action:"heatingDurationDown", icon:"st.thermostat.thermostat-down", backgroundColor:"#ffffff"
		}
        
        htmlTile(name:"chartHTML", action: "getImageChartHTML", width: 6, height: 4, whiteList: ["www.gstatic.com", "raw.githubusercontent.com"])
        
        main(["switch"])
    	details(["switch", "levelUp", "level", "heatingDuration", "heatingDurationUp", "levelDown", "heatingDurationDown", "presence", "currentHeatLevel", "refresh", "chartHTML", "network", "status"])
       
	}
}

mappings {
    path("/getImageChartHTML") {action: [GET: "getImageChartHTML"]}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"

}

// handle commands
def poll() {
	log.debug "Executing 'poll'"
    def resp = parent.apiGET("/devices/${device.deviceNetworkId.tokenize("/")[0]}?offlineView=true")
    if (resp.status != 200) {
		log.error("Unexpected result in poll(): [${resp.status}] ${resp.data}")
        setOffline()
		return []
	}
    if ((!resp.data.result.online) || (!resp.data.result.sensorInfo.connected)) { 
    	setOffline()
        return []
    }
    def currentHeatLevel = 0
    def nowHeating = false
    def targetHeatingLevel = 0
    def timer = 0
    def presenceStart = 0
    def presenceEnd = 0
    state.isOwner = (device.deviceNetworkId.tokenize("/")[1] == resp.data.result.ownerId)
    if (device.deviceNetworkId.tokenize("/")[1] == resp.data.result.leftUserId) {
    	state.bedSide = "left"
    	nowHeating = resp.data.result.leftNowHeating ? true : false
        currentHeatLevel = resp.data.result.leftHeatingLevel as Integer
        if (nowHeating) {
        	timer = resp.data.result.leftHeatingDuration
        }
        targetHeatingLevel = resp.data.result.leftTargetHeatingLevel as Integer
        presenceStart = resp.data.result.leftPresenceStart as Integer
        presenceEnd = resp.data.result.leftPresenceEnd as Integer
        
    } else {
    	state.bedSide = "right"
    	nowHeating = resp.data.result.rightNowHeating ? true : false
        currentHeatLevel = resp.data.result.rightHeatingLevel as Integer
        if (nowHeating) {
        	timer = resp.data.result.rightHeatingDuration
        }
        targetHeatingLevel = resp.data.result.rightTargetHeatingLevel as Integer
        presenceStart = resp.data.result.rightPresenceStart as Integer
        presenceEnd = resp.data.result.rightPresenceEnd as Integer
    }
    sendEvent(name: "switch", value: nowHeating ? "on" : "off")
    sendEvent(name: "level", value: targetHeatingLevel)
    
    state.desiredLevel = targetHeatingLevel as Integer
    sendEvent(name: "desiredLevel", "value": state.desiredLevel, unit: "%", displayed: false)
    
    sendEvent(name: "currentHeatLevel", value: currentHeatLevel)
    addCurrentHeatLevelToHistoricalArray(currentHeatLevel)
    def formattedTime = convertSecondsToString(timer)
    sendEvent(name: "timer", value: formattedTime, descriptionText: "Heating Timer ${formattedTime}", displayed: true)
    if (!state.heatingDuration) {
    	state.heatingDuration = 180
    	sendEvent("name":"heatingDuration", "value": convertSecondsToString(state.heatingDuration * 60), displayed: false)
    }
    sendEvent(name: "status", value: "Last update:\n" + Date.parse("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", resp.data.result.lastHeard).format("EEE, d MMM yyyy HH:mm:ss"), displayed: false )
    
    //BED PRESENCE LOGIC
    log.debug "Last 5 heat readings: $state.heatLevelHistory"
    def contactState = device.currentState("contact").getValue()
    def currSwitchState = device.currentState("switch").getValue()
    def heatDelta
    if (currSwitchState == "on") {
    	heatDelta = currentHeatLevel - state.desiredLevel
    } else {
        heatDelta = currentHeatLevel - 10
    }
    
    //If presence start value has changed then assume something is going on.
    if (state.lastPresenceStartValue) {
    	 if ((presenceStart != state.lastPresenceStartValue) && (contactState == "open")) {
         	sendEvent(name: "8slp Event", value: "${app.label}", displayed: true, isStateChange: true, descriptionText: "Presence start event received from 8Slp.")
        	//Stop wake up analysis if a bed presence event flagged by 8slp API.
        	stopWakeUpAnalysis()
            //Set recorded heat level on sleep
            state.heatLevelOnSleep = currentHeatLevel
            //Start skeep analysis to detect 'in bed' patterns.
            startSleepAnalysis()
        }
    }
    
    //If 8slp flags bed left event, start wake up analysis process in 7 minutes time.
    if (state.lastPresenceEndValue) {
    	if ((presenceEnd != state.lastPresenceEndValue) && (contactState == "closed")) {
        	sendEvent(name: "8slp Event", value: "${app.label}", displayed: true, isStateChange: true, descriptionText: "Presence end event received from 8Slp.")
        	//Set recorded heat level on wake up.
        	state.heatLevelOnWakeUp = currentHeatLevel
            runIn(7*60, startWakeUpAnalysis)
        }
    }
    
    //Sleep analysis logic. When does device know someone is in bed?
    if (state.analyzeSleep) {
    	//If bed temperature has risen or bed heat is above set level, likely someone is lying in bed. 
    	if ((currentHeatLevel - state.heatLevelOnSleep >= 5) || (heatDelta >= 8)) {
        	setInBed()
            stopSleepAnalysis()
            unschedule('stopSleepAnalysis')
        }
    }
    
    //Wake up analysis logic. When does device know someone is actually out of bed?
    if (state.analyzeWakeUp) {
    	//Has desired heat level been altered during sleep?
    	if (lastDesiredLevel != state.desiredLevel) {
    		state.desiredLevelChange = true
        }
        
        //Does heatlevel changes find any patterns?
    	if (state.lastCurrentHeatLevel) {
        	
            def bodyLeft = false
            //Check for substantial bed heat loss.
            if ((state.heatLevelHistory[0] < state.heatLevelHistory[1]) && (state.heatLevelHistory[1] < state.heatLevelHistory[2])) {
            	if ((state.heatLevelOnWakeUp - currentHeatLevel) >= 10) {
                	bodyLeft = true
                }
            }
            
        	//Bed is cooling fast, assume this warm body has left bed.
            if ((contactState == "closed") && (bodyLeft || ((heatDelta <= 5) && (heatDelta > -5)))) {
            	setOutOfBed()
                stopWakeUpAnalysis()
                unschedule('stopWakeUpAnalysis')
            }
        }
    }
    state.lastCurrentHeatLevel = currentHeatLevel
    state.lastDesiredLevel = state.desiredLevel
    state.lastPresenceStartValue = presenceStart
    state.lastPresenceEndValue = presenceEnd
    
    addHistoricalSleepToChartData()
    
}

def installed() {
	sendEvent(name: "contact", value: "open")
}

def refresh() {
	log.debug "Executing 'refresh'"
	poll()
}

def setInBed() {
	sendEvent(name: "contact", value: "closed", descriptionText: "Is In Bed", displayed: true)
}

def setOutOfBed() {
	sendEvent(name: "contact", value: "open", descriptionText: "Is Out Of Bed", displayed: true)
    state.desiredLevelChange = false
}

// Start wake up analysis logic over the next 37 minutes
def startWakeUpAnalysis() {
	log.debug "Starting wake up analysis"
	state.analyzeWakeUp = true
    runIn(37*60, stopWakeUpAnalysis)
}

def stopWakeUpAnalysis() {
	log.debug "Stopping wake up analysis"
	state.analyzeWakeUp = false
}

// Start sleep analysis logic over the next 17 minutes
def startSleepAnalysis() {
	log.debug "Starting sleep analysis"
	state.analyzeSleep = true
    runIn(17*60, stopSleepAnalysis)
}

def stopSleepAnalysis() {
	log.debug "Stopping sleep analysis"
	state.analyzeSleep = false
}

def on() {
	log.debug "Executing 'on'"
	// TODO: handle 'on' command
    def body
	if (state.bedSide && state.bedSide == "left") {
    	body = [ 
        	"leftHeatingDuration": "${state.heatingDuration * 60}"
        ]
	} else {
    	body = [ 
        	"rightHeatingDuration": "${state.heatingDuration * 60}"
        ]
    }
    parent.apiPUT("/devices/${device.deviceNetworkId.tokenize("/")[0]}", body)
    runIn(3, refresh)
}

def off() {
	log.debug "Executing 'off'"
	// TODO: handle 'off' command
    def body
	if (state.bedSide && state.bedSide == "left") {
    	body = [ 
        	"leftHeatingDuration": 0
        ]
	} else {
    	body = [ 
        	"rightHeatingDuration": 0
        ]
    }
    parent.apiPUT("/devices/${device.deviceNetworkId.tokenize("/")[0]}", body)
    runIn(3, refresh)
}

def setLevel(percent) {
	log.debug "Executing 'setLevel' with percent $percent"
	// TODO: handle 'setLevel' command
    if (percent < 10) {
		percent = 10
	}
	if (percent > 100) {
		percent = 100
	}
    def currSwitchState = device.currentState("switch").getValue()
    def body
	if (state.bedSide && state.bedSide == "left") {
    	if (currSwitchState == "on") {
    		body = [ 
        		"leftTargetHeatingLevel": percent
       	 	]
        } else {
        	body = [ 
        		"leftTargetHeatingLevel": percent,
                "leftHeatingDuration": "${state.heatingDuration * 60}"
       	 	]
        }
	} else {
    	if (currSwitchState == "on") {
    		body = [ 
        		"rightTargetHeatingLevel": percent
       	 	]
        } else {
        	body = [ 
        		"rightTargetHeatingLevel": percent,
                "rightHeatingDuration": "${state.heatingDuration * 60}"
       	 	]
        }
    }
    parent.apiPUT("/devices/${device.deviceNetworkId.tokenize("/")[0]}", body)
    sendEvent(name: "level", value: percent)
    runIn(4, refresh)
}

def levelUp() {
	log.debug "Executing 'levelUp'"
    def currentLevel = getLevel() as Integer
    def newLevel = (currentLevel + 10) - (currentLevel % 10)
    if (newLevel > 100) {
    	newLevel = 100
    }
	setNewLevelValue(newLevel)
}

def levelDown() {
	log.debug "Executing 'levelDown'"
    def currentLevel = getLevel() as Integer
    def newLevel = (currentLevel - 10) - (currentLevel % 10)
    if (newLevel < 10) {
    	newLevel = 10
    }
	setNewLevelValue(newLevel)
}

def getLevel() { 
	return state.desiredLevel == null ? device.currentValue("level") : state.desiredLevel
}

def setLevelToDesired() {
	setLevel(state.newLevel)
}

def setNewLevelValue(newLevelValue) {
	log.debug "Executing 'setNewLevelValue' with value $newLevelValue"
	unschedule('setLevelToDesired')
    state.newLevel = newLevelValue
    state.desiredLevel = state.newLevel
	sendEvent("name":"desiredLevel", "value": state.desiredLevel, displayed: true)
	log.debug "Setting level up to: ${state.newLevel}"
    runIn(3, setLevelToDesired)
}

//Commands
def setHeatDuration(minutes) {
	log.debug "Executing 'setHeatDuration with length $minutes minutes'"
    if (minutes < 10) {
		minutes = 10
	}
	if (minutes > 600) {
		minutes = 600
	}
    state.heatingDuration = minutes
    sendEvent("name":"heatingDuration", "value": convertSecondsToString(state.heatingDuration * 60), displayed: false)
}

def heatingDurationDown() {
	log.debug "Executing 'heatingDurationDown'"
    //Round down result
    def newHeatDurationLength = (state.heatingDuration - 15) - (state.heatingDuration % 15)
	setHeatDuration(newHeatDurationLength)
}

def heatingDurationUp() {
	log.debug "Executing 'heatingDurationUp'"
    //Round down result
    def newHeatDurationLength = (state.heatingDuration + 15) - (state.heatingDuration % 15)
	setHeatDuration(newHeatDurationLength)
}

def setOffline() {
	sendEvent(name: 'network', value: "Not Connected" as String)
    sendEvent(name: "switch", value: "offline")
}

//Helper methods
def convertSecondsToString(seconds) {
	def hour = (seconds / 3600) as Integer
    def minute = (seconds - (hour * 3600)) / 60 as Integer
    
    def hourString = (hour < 10) ? "0$hour" : "$hour"
    def minuteString = (minute < 10) ? "0$minute" : "$minute"
    
	return "${hourString}hr:${minuteString}mins"
}

def getTimeZone() {
	def tz = null
	if(location?.timeZone) { tz = location?.timeZone }
	if(!tz) { log.warn "No time zone has been retrieved from SmartThings. Please try to open your ST location and press Save." }
	return tz
}

def addCurrentHeatLevelToHistoricalArray(heatLevel) {
	if (!state.heatLevelHistory) state.heatLevelHistory = [heatLevel, heatLevel, heatLevel, heatLevel, heatLevel]
    state.heatLevelHistory.add(0, heatLevel)
    state.heatLevelHistory.pop()
}

//Chart data rendering
def getHistoricalSleepData(fromDate, toDate) {
	def result = ""
	if (state.isOwner) {
		result = parent.apiGET("/users/${device.deviceNetworkId.tokenize("/")[1]}/trends?tz=${URLEncoder.encode(getTimeZone().getID())}&from=${fromDate.format("yyyy-MM-dd")}&to=${toDate.format("yyyy-MM-dd")}")
    } else if (parent.partnerAuthenticated()) {
    	result = parent.apiGETWithPartner("/users/${device.deviceNetworkId.tokenize("/")[1]}/trends?tz=${URLEncoder.encode(getTimeZone().getID())}&from=${fromDate.format("yyyy-MM-dd")}&to=${toDate.format("yyyy-MM-dd")}")
    }
    result
}

def addHistoricalSleepToChartData() {
    def date = new Date()
	def resp = getHistoricalSleepData((date - 6), date)
    if (resp == "" || resp.status == 403) {
    	log.error("Cannot access sleep data for partner.")
        state.chartData = "UNAVAILABLE"
    } else if (resp.status != 200) {
    	log.error("Unexpected result in addHistoricalSleepToChartData(): [${resp.status}] ${resp.data}")
	}
    else {
    	def days = resp.data.days
        state.chartData = [0, 0, 0, 0, 0, 0, 0]
        state.chartData2 = [0, 0, 0, 0, 0, 0, 0]
        state.chartData3 = [0, 0, 0, 0, 0, 0, 0]
        
        0.upto(days.size() - 1, {
        	state.chartData[6 - it] = days[it].sleepDuration / 3600  
            state.chartData2[6 - it] = days[it].presenceDuration / 3600
            state.chartData3[6 - it] = days[it].score
        })
    }
}

def getImageChartHTML() {
	try {
    	def date = new Date()
		if (state.chartData == null) {
    		state.chartData = [0, 0, 0, 0, 0, 0, 0]
    	}
        def hData
        if (state.chartData == "UNAVAILABLE") { 
        	hData = """
            	<div class="centerText" style="font-family: helvetica, arial, sans-serif;">
				  <p>Sleep data unavailable for partner user.</p>
				  <p>Open the Eight Sleep (Connect) app to enter partner side credentials.</p>
				</div>
            """
        }
        else {
        	if (state.chartData2 == null) {
            	state.chartData2 = [0, 0, 0, 0, 0, 0, 0]
            }
            if (state.chartData3 == null) {
            	state.chartData3 = [0, 0, 0, 0, 0, 0, 0]
            }
            def a = state.chartData.max()
            def b = state.chartData2.max()
            def topValue = a > b ? a : b
			hData = """
	  <h4 style="font-size: 16px; font-weight: bold; text-align: center; background: #00a1db; color: #f5f5f5;">Historical Data</h4><br>
      <div id="main_graph" style="width: 100%; height: 260px;">
      	<img src="http://chart.googleapis.com/chart?cht=bvg&chs=368x200&chxt=x,y,y,r,r&chco=5c628f,00e2b1&chd=t2:${state.chartData.getAt(6)},${state.chartData.getAt(5)},${state.chartData.getAt(4)},${state.chartData.getAt(3)},${state.chartData.getAt(2)},${state.chartData.getAt(1)},${state.chartData.getAt(0)}|${state.chartData2.getAt(6)},${state.chartData2.getAt(5)},${state.chartData2.getAt(4)},${state.chartData2.getAt(3)},${state.chartData2.getAt(2)},${state.chartData2.getAt(1)},${state.chartData2.getAt(0)}|${state.chartData3.getAt(6)},${state.chartData3.getAt(5)},${state.chartData3.getAt(4)},${state.chartData3.getAt(3)},${state.chartData3.getAt(2)},${state.chartData3.getAt(1)},${state.chartData3.getAt(0)}&chds=0,${topValue+2},0,${topValue+2},0,100&chxl=0:|${(date - 6).format("d MMM")}|${(date - 5).format("d MMM")}|${(date - 4).format("d MMM")}|${(date - 3).format("d MMM")}|${(date - 2).format("d MMM")}|${(date - 1).format("d MMM")}|${date.format("d MMM")}|2:|Hrs|4:|Score&chxp=2,50|4,50&chxr=1,0,${topValue+2},2|3,0,100,20&chbh=a,0,5&chm=D,cc0099,2,,3&chdl=Sleep|Presence|Sleep%20Score&chco=5c628f,00e2b1,cc0099&chdlp=t">
      </div>
			"""
	    }
		def mainHtml = """
		<!DOCTYPE html>
		<html>
        	<head>
				<meta http-equiv="cache-control" content="max-age=0"/>
				<meta http-equiv="cache-control" content="no-cache"/>
				<meta http-equiv="expires" content="0"/>
				<meta http-equiv="expires" content="Tue, 01 Jan 1980 1:00:00 GMT"/>
				<meta http-equiv="pragma" content="no-cache"/>
				<meta name="viewport" content="width = device-width, user-scalable=no, initial-scale=1.0">

				<link rel="stylesheet prefetch" href="${getCssData()}"/>
			</head>
			<body>
            	${hData}
            </body>
			</html>
		"""
		render contentType: "text/html", data: mainHtml, status: 200
	}
	catch (ex) {
		log.error "getChartHTML Exception:", ex
	}
}

def getCssData() {
	def cssData = null
	def htmlInfo
	state.cssData = null

	if(htmlInfo?.cssUrl && htmlInfo?.cssVer) {
		if(state?.cssData) {
			if (state?.cssVer?.toInteger() == htmlInfo?.cssVer?.toInteger()) {
				//LogAction("getCssData: CSS Data is Current | Loading Data from State...")
				cssData = state?.cssData
			} else if (state?.cssVer?.toInteger() < htmlInfo?.cssVer?.toInteger()) {
				//LogAction("getCssData: CSS Data is Outdated | Loading Data from Source...")
				cssData = getFileBase64(htmlInfo.cssUrl, "text", "css")
				state.cssData = cssData
				state?.cssVer = htmlInfo?.cssVer
			}
		} else {
			//LogAction("getCssData: CSS Data is Missing | Loading Data from Source...")
			cssData = getFileBase64(htmlInfo.cssUrl, "text", "css")
			state?.cssData = cssData
			state?.cssVer = htmlInfo?.cssVer
		}
	} else {
		//LogAction("getCssData: No Stored CSS Info Data Found for Device... Loading for Static URL...")
		cssData = getFileBase64(cssUrl(), "text", "css")
	}
	return cssData
}

def getFileBase64(url, preType, fileType) {
	try {
		def params = [
			uri: url,
			contentType: '$preType/$fileType'
		]
		httpGet(params) { resp ->
			if(resp.data) {
				def respData = resp?.data
				ByteArrayOutputStream bos = new ByteArrayOutputStream()
				int len
				int size = 4096
				byte[] buf = new byte[size]
				while ((len = respData.read(buf, 0, size)) != -1)
					bos.write(buf, 0, len)
				buf = bos.toByteArray()
				//LogAction("buf: $buf")
				String s = buf?.encodeBase64()
				//LogAction("resp: ${s}")
				return s ? "data:${preType}/${fileType};base64,${s.toString()}" : null
			}
		}
	}
	catch (ex) {
		log.error "getFileBase64 Exception:", ex
	}
}

def cssUrl()	 { return "https://raw.githubusercontent.com/desertblade/ST-HTMLTile-Framework/master/css/smartthings.css" }