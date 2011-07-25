from elveos.member import Member
from elveos.feature import Feature

print 'Members:'
for member in Member.objects:
    print member.id
    
print 'Features:'
for feature in Feature.objects:
    print feature.id
    
