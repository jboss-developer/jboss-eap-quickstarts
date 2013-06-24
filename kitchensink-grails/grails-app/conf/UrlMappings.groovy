class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/"(controller: 'member', action: 'index')
		"500"(view:'/error')
        "/rest/member/$id"(controller: "memberRest") {
            action = [GET: "getMember"]
        }

        "/rest/members"(controller: "memberRest") {
            action = [GET: "getMembers"]
        }

	}
}
