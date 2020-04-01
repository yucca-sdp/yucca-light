/**
 * 
 */

var EventStateEnum = [ 
                       'TO SEND REALTIME ENDPOINT', 
                       'SENDING REALTIME IN PROGRESS', 
                       'SENDING FAILED (NETWORK ERRORS)', 
                       'SENT REALTIME SUCCESSFULL', 
                       'TO SEND A2A ENDPOINT',
                       'SENDING A2A IN PROGRESS', 
                       'SENT A2A SUCCSSFULL', 
                       'SENT INVALID (HTTP ERRORS)'
];

var workflowStateObj = {
		s0_to_send_rt: {
			name: "yucca_light.to_send_rt",
			countMessages:0,
			state: EventStateEnum[0],
			lastmessage: {
				payload: { },
				headers: { }
			},
			messages: []
		},
		s1_sending_rt_progress: {
			name: "yucca_light.sending_rt_progress",
			countMessages: 0,
			state: EventStateEnum[1],
			lastmessage: {
				payload: { },
				headers: { }
			},
			messages: []
		},
		s2_sent_rt: {
			name: "yucca_light.sent_rt",
			countMessages: 0,
			state: EventStateEnum[3],
			lastmessage: {
				payload: { },
				headers: { }
			},
			messages: []
		},
		s3_sending_failed: {
			name: "yucca_light.sending_failed",
			countMessages: 0,
			state: EventStateEnum[2],
			lastmessage: {
				payload: { },
				headers: { }
			},
			messages: []
		},
		s4_to_send_a2a: {
			name: "yucca_light.to_send_a2a",
			countMessages: 0,
			state: EventStateEnum[4],
			lastmessage: {
				payload: { },
				headers: { }
			},
			messages: []
		},
		s5_sending_a2a_progress: {
			name: "yucca_light.sending_a2a_progress",
			countMessages: 0,
			state: EventStateEnum[5],
			lastmessage: {
				payload: { },
				headers: { }
			},
			messages: []
		},
		s6_sent_a2a: {
			name: "yucca_light.sent_a2a",
			countMessages: 0,
			state: EventStateEnum[6],
			lastmessage: {
				payload: { },
				headers: { }
			},
			messages: []
		},
		s7_sent_invalid: {
			name: "yucca_light.sent_invalid",
			countMessages: 0,
			state: EventStateEnum[7],
			lastmessage: {
				payload: { },
				headers: { }
			},
			messages: []
		}
};

var workflowErrorStateObj = {
		
}