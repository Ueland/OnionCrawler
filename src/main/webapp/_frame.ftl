[#if isAdmin]
	[@includeTemplate name="admin/wrapper.ftl"/]
[#else]
	[@includeTemplate name="wrapper.ftl"/]
[/#if]